/*
 * Copyright (C) 2013 by danjian <josepwnz@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.coinbot.faucet;

/**
 * Temporizador para controlar los tiempos entre claims
 * @author danjian
 */
public class ClaimTimer implements Runnable {
	private Claim claim;
	private Thread thread;
	private boolean ready = true;
	private int hours = 0;
	private int minutes = 0;
	private int seconds = 0;
	
	public ClaimTimer(Claim claim) {
		this.claim = claim;
		this.hours = claim.getFaucet().getTimer()/60;
		this.minutes = claim.getFaucet().getTimer();
	}
	
	public void done(int reward, int timer) {
		this.hours = timer/60;
		this.minutes = timer;
		this.seconds = 0;
		claim.getFaucet().setTimer(timer);
		claim.getFaucet().setReward(reward);
		ready = false;
		thread = new Thread(this);
		thread.start();
	}
	
	public boolean isReady() {
		return ready;
	}
	
	public String getTimer() {
		String zeroH = "";
		String zeroM = "";
		String zeroS = "";
		if(hours < 10) {
			zeroH = "0";
		}
		
		if(minutes < 10) {
			zeroM = "0";
		}
		if(seconds < 10) {
			zeroS = "0";
		}
		
		return zeroH + hours + ":" + zeroM + minutes + ":" + zeroS + seconds;
	}
	
	@Override
	public void run() {
	
		claim.getPanel().getProgressBar().setMaximum(claim.getFaucet().getTimer()*60);
		claim.getPanel().getProgressBar().setValue(0);
		claim.getPanel().getProgressBar().setStringPainted(true);
		
		int s = claim.getFaucet().getTimer()*60;
		
		for (int i = 0; i < s; i++) {
			
			try {
				seconds--;
				
				if(seconds < 0) {
					minutes--;
					
					if(minutes < 0) {
						hours--;
						minutes = 59;
					}
					
					seconds = 59;
				}
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			claim.getPanel().getProgressBar().setString(getTimer());
			claim.getPanel().getProgressBar().setValue(i);
		}
		
		claim.getPanel().ready();
		ready = true;
	}
	
}
