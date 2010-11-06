package ch.zhaw.cctd.domain;

public class Round {

	private int startTime;
	private Wave wave;
	public Round(int startTime, Wave wave) {
		this.setStartTime(startTime);
		this.setWave(wave);
	}
	private void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	public int getStartTime() {
		return startTime;
	}
	private void setWave(Wave wave) {
		this.wave = wave;
	}
	public Wave getWave() {
		return wave;
	}
	
}
