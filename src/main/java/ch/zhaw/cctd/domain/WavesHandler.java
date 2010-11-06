package ch.zhaw.cctd.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.zhaw.cctd.domain.prototype.WavePrototype;

/**
 * This is the Thread that is handling the differend Waves. The Thread is waiting 
 * @author Rolf
 *
 */
public class WavesHandler implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(WavesHandler.class);
	
	private long lastWaveSchedule;
	private int waveCount;
	//eine Map<Zeit, Wave> der XML Wave Konfiguration. Zu welchem Zeitpunkt in Sekunden nach Spielstart wird welche Wave gestartet wird.
	private ArrayList<Wave> wavesData;
	private boolean working;
	private Match match;
	
	public boolean isWorking() {
		return working;
	}

	public void setWorking(boolean working) {
		this.working = working;
	}

	public WavesHandler(Match match, List<WavePrototype> waveprototypes) {
		
		this.match = match;		
		this.wavesData = new ArrayList<Wave>();
		for (WavePrototype p : waveprototypes) {
			Wave w = new Wave(match, p);
			this.wavesData.add(w);
		}
	}

	@Override
	public void run() {

		logger.debug("Thread started");
		
		//Setting Working Flag
		setWorking(true);
		
		// Do the Wavehandling
		startWaveHandling();

		
	}

	protected void startWaveHandling() {
		//Initialise the lastWaveSchedule with the Time this instance is started
		this.lastWaveSchedule = Calendar.getInstance().getTimeInMillis();
		

		
		for(waveCount = 0; waveCount < wavesData.size(); waveCount++) {
			if(logger.isTraceEnabled()) logger.trace("Spawning Wave:" + waveCount + " of " + wavesData.size() + " Waves");
			try {
	        	//Wait until the Wave has to be Spawned
	        	
	        	//Calculate the exact Wait time
	        	//get the current Time
	        	long currentTime = Calendar.getInstance().getTimeInMillis();
	        	//Calculate the time Correction
	        	long timeCorrection = currentTime - this.lastWaveSchedule;
	        	//Calculate the Final Wait time
	        	long timeToWait = wavesData.get(waveCount).getSpawnTime() * 1000 - timeCorrection;

	        	if(logger.isTraceEnabled())logger.trace("Thread Sleeping at Wave Spawning for " + timeToWait);
	        	if(timeToWait>0) {
	        		Thread.sleep(timeToWait);
	        	}
	        	
	        	//Check if Thread should stop
	        	if(!working) {
	     
	        		throw new InterruptedException("Stopped by stop()");
	        	}

	        		
	        	logger.debug("Wave started: {}", wavesData.get(waveCount).getName());
	        	//Save the time of the last Wave Schedule for next Wave
	        	this.lastWaveSchedule = Calendar.getInstance().getTimeInMillis();
	        	
	        	//Start Spawning the current Wave
	        	wavesData.get(waveCount).startSpawn(this, match);
	        	
	        	//Wave Spawn finished. Go to next one.
	          
	        }
	        catch(InterruptedException e) {
	        	logger.error("InterruptedException in WaveHandler Thread", e);
	        	break;
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	      }
	}

	public void stop() {
		Thread thisThread = Thread.currentThread();
		setWorking(false);
		if(thisThread != null) {
			thisThread.interrupt();
		}
		
	}
	
	

}
