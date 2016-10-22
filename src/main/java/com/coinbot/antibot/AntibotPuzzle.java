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
package com.coinbot.antibot;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class AntibotPuzzle {
	protected BufferedImage puzzleImage;
	protected List<AntibotPuzzleLink> links = new ArrayList<AntibotPuzzleLink>();
	
	public AntibotPuzzle(BufferedImage puzzle, List<AntibotPuzzleLink> links) {
		this.puzzleImage = puzzle;
		this.links = links;
	}
	
	public void setOrder(int[] order) {
		for (int i = 0; i < links.size(); i++) {
			links.get(i).setOrder(order[i]);
		}
	}
	
	public List<AntibotPuzzleLink> getLinks() {
		return links;
	}
	
	public BufferedImage getPuzzleImage() {
		return puzzleImage;
	}
}