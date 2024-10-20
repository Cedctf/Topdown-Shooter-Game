package Event;

import Player.Player;
import Map.Camera;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseHandler extends MouseAdapter {
    private Player player;
    private Camera camera;

    public MouseHandler(Player player, Camera camera) {
        this.player = player;
        this.camera = camera;
    }

    private void mouseUpdate(int mouseX, int mouseY) {
        // 判断角色朝向
        int playerCenterX = (player.getX() - camera.getX()) + player.getRec().width / 2;
        if (mouseX < playerCenterX) {
            player.setFacingRight(false);
        } else {
            player.setFacingRight(true);
        }

        // 获取鼠标位置
        if (player.getCurrentWeapon() != null) {
            player.updateWeaponAngle(mouseX + camera.getX(), mouseY + camera.getY());
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!KeyHandler.isPaused() && !KeyHandler.isStop()) {
            mouseUpdate(e.getX(), e.getY());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!KeyHandler.isPaused() && !KeyHandler.isStop()) {
            mouseUpdate(e.getX(), e.getY());
            if (!player.isReloading) {
                player.isShooting = true;
                player.getCurrentWeapon().Shoot(e.getX() + camera.getX(), e.getY() + camera.getY());
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!KeyHandler.isPaused() && !KeyHandler.isStop()) {
            mouseUpdate(e.getX(), e.getY());
            if (player.isShooting) {
                player.getCurrentWeapon().Shoot(e.getX() + camera.getX(), e.getY() + camera.getY());
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!KeyHandler.isPaused() && !KeyHandler.isStop()) {
            player.isShooting = false;
        }
    }
}
