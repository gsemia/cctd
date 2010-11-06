package ch.zhaw.cctd.domain;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import ch.zhaw.cctd.domain.prototype.Prototype;
import ch.zhaw.cctd.domain.prototype.PrototypeList;
import ch.zhaw.cctd.domain.prototype.WavePrototype;
import ch.zhaw.cctd.event.EventHandler;
import ch.zhaw.cctd.xml.XmlConfigReader;


/**
 * Responsible for the whole Game. This Class holds together all main Game: Map + UI + PlayerSet + wavesHandler
 * 
 * Match is a Singleton. Use Match.getInstance() to get the Instance.
 * 
 * @author Rolf
 *
 */
public class Match {
		
	//Vars	
	Thread wavesHandlerThread;
	private Collection<Player> playerSet;
	private Map map;
	private PrototypeList prototypes;
	private WavesHandler wavesHandler;
	private EventHandler eventhandler;
	
	public Match(EventHandler eventhandler, InputStream stream) 
		throws ParserConfigurationException, SAXException, IOException
	{
		this.eventhandler = eventhandler;
		this.prototypes = XmlConfigReader.readData(stream);
	}
	
	public PrototypeList getPrototypeList() {
		return this.prototypes;
	}
	
	public <T extends Prototype> T getPrototype(Class<T> c, String id) {
		return this.prototypes.getPrototype(c, id);
	}
	
	public EventHandler getEventHandler() {
		return this.eventhandler;
	}
	
	public void startMatch() {
		setMap(new Map(this));
		wavesHandler = new WavesHandler(this, 
				this.prototypes.getPrototypes(WavePrototype.class));
		//Start the Wavehandler
		wavesHandlerThread = new Thread(wavesHandler);
		wavesHandlerThread.start();
		//TODO: Tell UI to Print itself
		
		//The game Starts now
	}
	
	public void addPlayers(Player ... players) {
		for(Player p : players) {
			playerSet.add(p);
		}
	}
	
	public Player getLocalPlayer() {
		for(Player p : playerSet) {
			if(p.isLocal()) {
				return p;
			}
		}
		throw new IllegalStateException("No Local Player available yet"); 
	}
	
	public void stopMatch() {
		//TODO: Notify all Elements to stop
		//TODO: Stop all Threads 
		wavesHandler.stop();
	}
	

	void setMap(Map map) {
		this.map = map;
	}

	public Map getMap() {
		return map;
	}
	
}
