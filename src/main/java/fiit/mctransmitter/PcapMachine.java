package fiit.mctransmitter;

import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.IpV4Rfc1349Tos;
import org.pcap4j.packet.UdpPacket;
import org.pcap4j.packet.UnknownPacket;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.packet.namednumber.IpNumber;
import org.pcap4j.packet.namednumber.IpVersion;
import org.pcap4j.packet.namednumber.UdpPort;
import org.pcap4j.util.MacAddress;

public class PcapMachine {

	private static final String INVALID_INTERVAL = "Defined interval must be a positive integer.";
	private static final String INVALID_TEXT = "There must be at least an one character in the text field.";
	private static final String INVALID_MULTICAST = "The address must be a valid multicast address in IPv4 notation (for example 224.26.10.5).";
	private static final String ERROR_INTERFACE = "Aa error occured during the polling of the network interface.";
	private static final String START_ERROR = "'";
	private static final String STOP_ERROR = "': ";
	private static final String CHARSET = "UTF-8";
	private static final int AWAIT_TERMINATION = 5000;
	private static final int READ_TIMEOUT = 10;
	private static final int SNAPLEN = 65536;
	private static final short SOURCE_PORT = 25000;
	private static final short DESTINATION_PORT = 780;
	private static final SimpleDateFormat SDF = new SimpleDateFormat("HH:mm:ss.SSS");
	
	private final ExceptionBuffer exceptionBuffer = new ExceptionBuffer();
	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	private Window window;
	private Inet4Address sourceIpAddress;
	private Inet4Address destinationIpAddress;
	private MacAddress sourceMacAddress;
	private MacAddress destinationMacAddress;
	private Integer interval;
	private String text;
	private PcapHandle sendHandle;
	private EthernetPacket packet;
	
	public PcapMachine(Window window, String destinationIpAddress, String interval, String text) 
			throws ExceptionBuffer, SocketException, PcapNativeException {
		setWindow(window);
		setDestinationIpAddress(destinationIpAddress);
		setInterval(interval);
		setText(text);
		exceptionBuffer.throwIfItIsNeeded();
		setSourceMacAddress();
		setSourceIpAddress();
		setDestinationMacAddress();
		setPcapHandler();
		buildFrame();
	}
	
	private void setWindow(Window window) {
		this.window = window;
	}
	
	private void setSourceMacAddress() throws SocketException {
		this.sourceMacAddress = MacAddress.getByAddress(findNetworkInterface().getHardwareAddress());
	}
	
	private void setSourceIpAddress() throws SocketException {
		this.sourceIpAddress = (Inet4Address)Collections.list(findNetworkInterface().getInetAddresses())
			 .stream()
			.filter(ip -> ip instanceof Inet4Address)
			.findAny()
			.get();
	}	
	
	private NetworkInterface findNetworkInterface() throws SocketException {
		List<NetworkInterface> nets = Collections.list(NetworkInterface.getNetworkInterfaces());
		Optional<NetworkInterface> adapterExistence = nets.parallelStream().filter((net) -> {
			try {
				return !net.isLoopback() && !net.isVirtual() && net.getHardwareAddress() != null
						&& Collections.list(net.getInetAddresses()).parallelStream()
								.filter(ip -> ip instanceof Inet4Address).findAny().isPresent();
			} catch (Exception e) {
				return false;
			}
		}).findFirst();
		if (adapterExistence.isPresent()) {
			return adapterExistence.get();
		} else {
			throw new SocketException(ERROR_INTERFACE);
		}
	}
	
	private void setDestinationMacAddress() {
		byte[] finalAddress = new byte[6];
		byte[] ipBytes = destinationIpAddress.getAddress();
		finalAddress[0] = 1;
		finalAddress[1] = 0;
		finalAddress[2] = 94;
		finalAddress[3] = (byte)(ipBytes[1]&127);
		finalAddress[4] = ipBytes[2];
		finalAddress[5] = ipBytes[3];
		this.destinationMacAddress = MacAddress.getByAddress(finalAddress);
	}
	
	private void setDestinationIpAddress(String destinationIpAddress) {
		try {
			Inet4Address address = (Inet4Address) Inet4Address.getByName(destinationIpAddress);
			if(address.isMulticastAddress()) {
				this.destinationIpAddress = address;
			} else {
				exceptionBuffer.addException(new IllegalArgumentException(START_ERROR + destinationIpAddress + STOP_ERROR + INVALID_MULTICAST));
			}
		} catch(UnknownHostException ex) {
			exceptionBuffer.addException(new IllegalArgumentException(START_ERROR + destinationIpAddress + STOP_ERROR + INVALID_MULTICAST));
		}
	}
	
	private void setInterval(String interval) {
		try {
			Integer intervalValue = Integer.parseInt(interval);
			if(intervalValue>0) {
				this.interval = intervalValue;
			} else {
				exceptionBuffer.addException(new IllegalArgumentException(START_ERROR + interval + STOP_ERROR + INVALID_INTERVAL));
			}
		} catch(NumberFormatException ex) {
			exceptionBuffer.addException(new NumberFormatException(START_ERROR + interval + STOP_ERROR + INVALID_INTERVAL));
		}
	}
	
	private void setText(String text) {
		if(text.length()>0) {
			this.text = text;
		} else {
			exceptionBuffer.addException(new IllegalArgumentException(START_ERROR + text + STOP_ERROR + INVALID_TEXT));
		}
	}
	
	private void setPcapHandler() throws PcapNativeException {
		PcapNetworkInterface nif = Pcaps.getDevByAddress(sourceIpAddress);
		sendHandle = nif.openLive(SNAPLEN, PromiscuousMode.PROMISCUOUS, READ_TIMEOUT);
	}
	
	private void buildFrame() {
		byte[] payload = text.getBytes(Charset.forName(CHARSET));
		UnknownPacket.Builder dataBuilder = new UnknownPacket.Builder();
		dataBuilder.rawData(payload);
		UdpPacket.Builder udpBuilder = new UdpPacket.Builder();
		udpBuilder.srcAddr(sourceIpAddress)
			.dstAddr(destinationIpAddress)
			.srcPort(new UdpPort(SOURCE_PORT,"emp-multicast"))
			.dstPort(new UdpPort(DESTINATION_PORT,"service-multicast"))
			.payloadBuilder(dataBuilder)
			.correctChecksumAtBuild(true)
			.correctLengthAtBuild(true);
		IpV4Packet.Builder ipBuilder = new IpV4Packet.Builder();
		ipBuilder.dstAddr(destinationIpAddress)
			.srcAddr(sourceIpAddress)
			.version(IpVersion.IPV4)
			.protocol(IpNumber.UDP)
			.tos(IpV4Rfc1349Tos.newInstance((byte) 0))
			.ttl((byte)255)
			.payloadBuilder(udpBuilder)				
			.correctChecksumAtBuild(true)
			.correctLengthAtBuild(true)
			.paddingAtBuild(true);
		ipBuilder.build();
		EthernetPacket.Builder etherBuilder = new EthernetPacket.Builder();
		etherBuilder.dstAddr(destinationMacAddress)
			.srcAddr(sourceMacAddress)
			.type(EtherType.IPV4)
			.payloadBuilder(ipBuilder)
			.paddingAtBuild(true);
		packet = etherBuilder.build();
	}

	public void start() {		
		executor.scheduleAtFixedRate(() -> {
			try {
				sendHandle.sendPacket(packet);
				StringBuffer areaLine = new StringBuffer();
				areaLine.append("time: '");
				areaLine.append(SDF.format(new Date()));
				areaLine.append("', destination: '");
				areaLine.append(destinationIpAddress.toString());
				areaLine.append("', text: '");
				areaLine.append(text);
				areaLine.append("' ");
				window.addLineToArea(areaLine.toString());
			} catch (PcapNativeException | NotOpenException e) {
				e.printStackTrace();
				executor.shutdownNow();
			}
		},0,interval,TimeUnit.MILLISECONDS);
	}
	
	public void stop() {
		if(executor!=null) {
			try {
				executor.shutdown();
				executor.awaitTermination(AWAIT_TERMINATION, TimeUnit.MILLISECONDS);
			} catch(InterruptedException e) {
			} finally {
				executor.shutdownNow();
			}
		}
	}
	
}
