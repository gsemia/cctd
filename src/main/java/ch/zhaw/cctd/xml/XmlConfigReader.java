package ch.zhaw.cctd.xml;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ch.zhaw.cctd.domain.prototype.CreepPrototype;
import ch.zhaw.cctd.domain.prototype.Prototype;
import ch.zhaw.cctd.domain.prototype.PrototypeList;
import ch.zhaw.cctd.domain.prototype.TowerPrototype;
import ch.zhaw.cctd.domain.prototype.TowerUpgradePrototype;
import ch.zhaw.cctd.domain.prototype.WavePrototype;

public final class XmlConfigReader {
	
	private static final Logger LOG = Logger.getLogger(XmlConfigReader.class);
	
	public static final PrototypeList readData(final String filename) 
		throws ParserConfigurationException, SAXException, IOException
	{
		return XmlConfigReader.readData(new File(filename));
	}
	
	public static final PrototypeList readData(final File file) 
		throws ParserConfigurationException, SAXException, IOException 
	{
		return XmlConfigReader.readData(new FileInputStream(file));
	}
	
	public static final PrototypeList readData(final InputStream stream) 
		throws ParserConfigurationException, SAXException, IOException 
	{
		return XmlConfigReader.getInstance().readDataInternal(stream);
	}
	
	private static final XmlConfigReader reader = new XmlConfigReader();
	private static final XmlConfigReader getInstance() {
		return reader;
	}
	
	public final PrototypeList readDataInternal(final InputStream stream)
		throws ParserConfigurationException, SAXException, IOException 
	{
		if (LOG.isDebugEnabled()) 
			LOG.debug("reading data from inputstream");
		Element root = this.getRootElement(stream);
		// String version = root.getAttribute("version");
		NodeList elements = root.getChildNodes();
		List<Prototype> data = new ArrayList<Prototype>();
		for (XmlNodeListIterator it = new XmlNodeListIterator(elements);it.hasNext();) {
			Node node = it.next();
			String nodename = node.getNodeName();
			String methodName = "handle"+nodename.toUpperCase().charAt(0)+nodename.substring(1);
			try {
				Method handleMethod = this.getClass().getDeclaredMethod(methodName, NodeList.class);
				@SuppressWarnings("unchecked")
				List<Prototype> gendata = (List<Prototype>)handleMethod.invoke(this, node.getChildNodes());
				data.addAll(gendata);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e);
				// TODO unsupported tag!
			} catch (IllegalArgumentException e) { 
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) { 
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) { 
				throw new RuntimeException(e); 
			}
		}
		PrototypeList list = new PrototypeList(data);
		return list;
	}
	
	private final Element getRootElement(final InputStream stream) 
		throws ParserConfigurationException, SAXException, IOException 
	{
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document d = db.parse(stream);
		return d.getDocumentElement();
	}
	
	private final Map<String,String> subhandleChilds(Map<String,String> data, Node node) {
		return this.subhandleChilds(data, node, "");
	}
	
	private final Map<String,String> subhandleChilds(Map<String,String> data, Node node, String prefix) {
		if (LOG.isDebugEnabled()) 
			LOG.debug("reading data from node "+node.getNodeName()+" with prefix: "+prefix);
		for (XmlNodeListIterator it = new XmlNodeListIterator(node.getChildNodes());it.hasNext();) {
			Node child = it.next();
			String newprefix = new StringBuffer(prefix).append(child.getNodeName()).append(".").toString();
			if (child.hasChildNodes()) {
				if (
						child.getChildNodes().getLength() == 1 &&
						child.getChildNodes().item(0).getNodeType() == Node.TEXT_NODE
				) {
					data.put(newprefix+"#text", child.getChildNodes().item(0).getNodeValue());
					if (LOG.isDebugEnabled())
						LOG.debug("adding "+newprefix+"#text->"+child.getChildNodes().item(0).getNodeValue());
				} else {
					this.subhandleChilds(data, child, newprefix);
				}
			}
			if (child.hasAttributes()) {
				NamedNodeMap attributes = child.getAttributes();
				if (attributes.getLength() > 1) {
					// if there are >1 attributes, a index must be generated because a simple list isn't enough
					String counterKey = newprefix.substring(0, newprefix.length()-1);
					int counter = (data.containsKey(counterKey)) ? Integer.parseInt(data.get(counterKey)):0;
					newprefix = new StringBuffer(counterKey).append(".").append(counter).append(".").toString();
					counter++;
					data.put(counterKey, ""+counter);
					if (LOG.isDebugEnabled()) 
						LOG.debug("adding "+counterKey+"->"+counter);
				}
				for (XmlNamedNodeMapIterator attIt = new XmlNamedNodeMapIterator(attributes);attIt.hasNext();) {
					Node att = attIt.next();
					String key = newprefix + att.getNodeName();
					String content = att.getNodeValue();
					if (data.containsKey(key)) {
						content = data.get(key)+";"+att.getNodeValue();
					}
					data.put(key, content);
					if (LOG.isDebugEnabled()) 
						LOG.debug("adding "+key+"->"+content);
				}
			}
		}
		return data;
	}
	
	@SuppressWarnings("unused")
	private final List<Prototype> handleTowers(final NodeList nodes) {
		if (LOG.isDebugEnabled())
			LOG.debug("handling towers");
		List<Prototype> data = new ArrayList<Prototype>();
		for (XmlNodeListIterator it = new XmlNodeListIterator(nodes);it.hasNext();) {
			if (LOG.isDebugEnabled())
				LOG.debug("found a tower:");
			Node towerNode = it.next();
			Map<String,String> tower = new HashMap<String,String>();
			// parse attributes
			NamedNodeMap attributes = towerNode.getAttributes();
			tower.put("id", attributes.getNamedItem("id").getNodeValue());
			tower.put("title", attributes.getNamedItem("title").getNodeValue());
			if (LOG.isDebugEnabled()) {
				LOG.debug("adding id->"+attributes.getNamedItem("id").getNodeValue());
				LOG.debug("adding title->"+attributes.getNamedItem("title").getNodeValue());
			}
			// parse upgrades
			NodeList towerChilds = towerNode.getChildNodes();
			List<TowerUpgradePrototype> upgrades = null;
			for (XmlNodeListIterator childIt = new XmlNodeListIterator(towerChilds);childIt.hasNext();) {
				Node child = childIt.next();
				if ("upgrades".equals(child.getNodeName())) {
					upgrades = this.subhandleTowerUpgrades(child.getChildNodes());
					break;
				}
			}
			// parse rest
			data.add(new TowerPrototype(this.subhandleChilds(tower, towerNode), upgrades));
		}
		return data;
	}
	
	private final List<TowerUpgradePrototype> subhandleTowerUpgrades(final NodeList nodes) {
		if (LOG.isDebugEnabled())
			LOG.debug("adding upgrades to tower");
		List<TowerUpgradePrototype> data = new ArrayList<TowerUpgradePrototype>();
		for (XmlNodeListIterator it = new XmlNodeListIterator(nodes);it.hasNext();) {
			Node upgradeNode = it.next();
			Map<String,String> upgrade = new HashMap<String,String>();
			if (LOG.isDebugEnabled())
				LOG.debug("adding upgrade to tower:");
			// parse attributes
			NamedNodeMap attributes = upgradeNode.getAttributes();
			upgrade.put("id", attributes.getNamedItem("id").getNodeValue());
			if (LOG.isDebugEnabled())
				LOG.debug("adding id->"+attributes.getNamedItem("id").getNodeValue());
			data.add(new TowerUpgradePrototype(this.subhandleChilds(upgrade, upgradeNode)));
		}
		return data;
	}
	
	@SuppressWarnings("unused")
	private final List<Prototype> handleCreeps(final NodeList nodes) {
		if (LOG.isDebugEnabled())
			LOG.debug("handling creeps");
		List<Prototype> data = new ArrayList<Prototype>();
		for (XmlNodeListIterator it = new XmlNodeListIterator(nodes);it.hasNext();) {
			Node creepNode = it.next();
			if (LOG.isDebugEnabled())
				LOG.debug("adding creep:");
			Map<String,String> creep = new HashMap<String,String>();
			NamedNodeMap attributes = creepNode.getAttributes();
			creep.put("id", attributes.getNamedItem("id").getNodeValue());
			creep.put("title", attributes.getNamedItem("title").getNodeValue());
			if (LOG.isDebugEnabled()) {
				LOG.debug("adding id->"+attributes.getNamedItem("id").getNodeValue());
				LOG.debug("adding title->"+attributes.getNamedItem("title").getNodeValue());
			}
			data.add(new CreepPrototype(this.subhandleChilds(creep, creepNode)));
		}
		return data;
	}
	
	@SuppressWarnings("unused")
	private final List<Prototype> handleWaves(final NodeList nodes) {
		if (LOG.isDebugEnabled())
			LOG.debug("handling waves");
		List<Prototype> data = new ArrayList<Prototype>();
		for (XmlNodeListIterator it = new XmlNodeListIterator(nodes);it.hasNext();) {
			Node waveNode = it.next();
			if (LOG.isDebugEnabled())
				LOG.debug("adding wave:");
			Map<String,String> wave = new HashMap<String,String>();
			NamedNodeMap attributes = waveNode.getAttributes();
			wave.put("id", attributes.getNamedItem("id").getNodeValue());
			if (LOG.isDebugEnabled())
				LOG.debug("adding id->"+attributes.getNamedItem("id").getNodeValue());
			data.add(new WavePrototype(this.subhandleChilds(wave, waveNode)));
		}
		return data;
	}
	
	@SuppressWarnings("unused")
	private final List<Prototype> handleMap(final NodeList nodes) {
		if (LOG.isDebugEnabled())
			LOG.debug("handling map");
		List<Prototype> data = new ArrayList<Prototype>();
		for (XmlNodeListIterator it = new XmlNodeListIterator(nodes);it.hasNext();) {
			Node node = it.next();
			
		}
		return data; // TODO
	}
	
}
