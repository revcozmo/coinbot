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
package com.coinbot.core;

import java.util.ArrayList;
import java.util.List;

import com.coinbot.faucet.Claim;
import com.coinbot.faucet.Faucet;
/**
 * Esta clase se usa para asociar cada proxy con una faucet y direccion btc
 * de forma que los claims sean unicos y no se repitan
 * @author danjian
 */
public class ClaimController {
	private List<Claim> queue = new ArrayList<Claim>();
	
	public void toQueue(Claim claim) {
		queue.add(claim);
		CoinbotApplication.ui.claimQueue.addClaim(claim);
	}
	
	public void deQueue(Claim claim) {
		queue.remove(claim);
		CoinbotApplication.ui.claimQueue.removeClaim(claim);
	}
	
	public Claim next() {
		List<Claim> cs = getClaims();
		
		if(!cs.isEmpty()) {
			Claim claim = cs.get(0);
			if(claim.getTimer().isReady()) {
				deQueue(claim);
				return claim;
			}
		}
		
		return null;
	}
	
	public void start() {
		// Asignacion de claims
		
		List<String> address = CoinbotApplication.addressDatabase.getAddresses();
		List<Faucet> faucets = CoinbotApplication.faucetDatabase.getFaucets();
		
		for (Faucet f : faucets) {
			int index =  0 + (int)(Math.random() * address.size()); 
			String a = address.get(index);
			Claim c = new Claim(f, a);
			toQueue(c);
		}
	}
	
	public List<Claim> getClaims() {
		return new ArrayList<Claim>(queue);
	}
	
	public int size() {
		return queue.size();
	}
}