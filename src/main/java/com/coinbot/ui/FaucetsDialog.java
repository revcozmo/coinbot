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
package com.coinbot.ui;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.coinbot.core.CoinbotApplication;
import com.coinbot.database.Address;
import com.coinbot.faucet.Currency;
import com.coinbot.faucet.Faucet;

import javax.swing.JTabbedPane;

import net.miginfocom.swing.MigLayout;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class FaucetsDialog extends JDialog {
	private static final long serialVersionUID = -2938299019512534236L;
	private List<JTextArea> areas = new ArrayList<JTextArea>();
	
	public FaucetsDialog() {
		super(CoinbotApplication.ui.frame, "Faucets");
		setModal(true);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		for (Currency currency : Currency.values()) {
			JPanel panel = new JPanel();
			panel.setLayout(new MigLayout("", "[350px]", "[300px]"));
			JScrollPane scroll = new JScrollPane();
			List<Faucet> faucetList = CoinbotApplication.faucetsDatabase
					.getFaucetsCurrency(currency);
			String fs = "";
			for (Faucet faucet : faucetList) {
				fs+=faucet.getUrl()+"\n";
			}
			JTextArea area = new JTextArea(fs);
			area.setName(currency.toString());
			areas.add(area);
			scroll.setViewportView(area);
			panel.add(scroll, "cell 0 0,grow");
			tabbedPane.add(currency.toString(), panel);
		}
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		panel_1.add(btnCancel);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
				dispose();
			}
		});
		panel_1.add(btnSave);
		
		pack();
		setLocationRelativeTo(CoinbotApplication.ui.frame);
		setVisible(true);
	}
	
	public void save() {
		List<Faucet> saveList = new ArrayList<Faucet>();
		for (JTextArea area : areas) {
			Currency c = Currency.valueOf(area.getName());
			String[] faucets = area.getText().split("\n");
			for (String url : faucets) {
				if(!url.isEmpty()) {
					saveList.add(new Faucet(c, url));
				}
			}
		}
		CoinbotApplication.faucetsDatabase.setFaucets(saveList);
		CoinbotApplication.faucetsDatabase.save();
	}
}
