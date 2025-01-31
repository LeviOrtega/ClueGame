package gui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import clueGame.Board;


public class MouseBoard implements MouseListener{

	@Override
	public void mouseClicked(MouseEvent e) {
		Point point = e.getPoint();
		Board.getInstance().handleBoardClickLogic(point);
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

}
