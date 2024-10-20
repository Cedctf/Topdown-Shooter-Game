package Collision;

import GameObject.GameObject;
import Player.Player;
import Sound.Sound;
import Weapons.Bullets.Bullets;
import Zombie.Zombie.*;
import Zombie.Devil.*;
import Zombie.ZombieDog.*;
import Map.Map;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class CollisionChecker {

    private Map map;

    public CollisionChecker(Map map) {
        this.map = map;
    }

    public boolean checkCollision(Rectangle objectRect) {
        int tileSize = map.getTileSize();
        int startTileX = objectRect.x / tileSize;
        int startTileY = objectRect.y / tileSize;
        int endTileX = (objectRect.x + objectRect.width - 1) / tileSize;
        int endTileY = (objectRect.y + objectRect.height - 1) / tileSize;

        for (int tileX = startTileX; tileX <= endTileX; tileX++) {
            for (int tileY = startTileY; tileY <= endTileY; tileY++) {
                if (tileX >= 0 && tileX < map.getMaxWorldCol() && tileY >= 0 && tileY < map.getMaxWorldRow()) {
                    int tileNum = map.getMap1TileNum()[tileX][tileY];
                    if (tileNum >= 0 && tileNum < map.getTile().length && map.getTile()[tileNum] != null) {
                        if (map.getTile()[tileNum].collision) {
                            Rectangle tileRect = new Rectangle(tileX * tileSize, tileY * tileSize, tileSize, tileSize);
                            if (objectRect.intersects(tileRect)) {
                                return true; // Collision detected
                            }
                        }
                    }
                }
            }
        }
        return false; // No collision
    }

    public boolean checkCollisionWithMap(GameObject object) {
        Rectangle objectRect = object.getRec();
        int tileSize = map.getTileSize();
        int startTileX = objectRect.x / tileSize;
        int startTileY = objectRect.y / tileSize;
        int endTileX = (objectRect.x + objectRect.width - 1) / tileSize;
        int endTileY = (objectRect.y + objectRect.height - 1) / tileSize;

        for (int tileX = startTileX; tileX <= endTileX; tileX++) {
            for (int tileY = startTileY; tileY <= endTileY; tileY++) {
                if (tileX >= 0 && tileX < map.getMaxWorldCol() && tileY >= 0 && tileY < map.getMaxWorldRow()) {
                    int tileNum = map.getMap1TileNum()[tileX][tileY];
                    if (tileNum >= 0 && tileNum < map.getTile().length && map.getTile()[tileNum] != null) {
                        if (map.getTile()[tileNum].collision) {
                            Rectangle tileRect = new Rectangle(tileX * tileSize, tileY * tileSize, tileSize, tileSize);
                            if (objectRect.intersects(tileRect)) {
                                return true; // Collision detected
                            }
                        }
                    }
                }
            }
        }
        return false; // No collision
    }


    public void checkCollisions(Player player, ArrayList<Zombie> zombies, ArrayList<Devil> devils, ArrayList<ZombieDog> zombieDogs) {
        checkPlayerEnemyCollision(player, zombies, devils, zombieDogs);
        checkEnemyCollisions(zombies, devils, zombieDogs);
        checkPlayerEnemyDamage(player, zombies, devils, zombieDogs);
        checkPlayerBulletCollision(player, zombies, devils, zombieDogs);
        checkDevilBulletCollision(player, devils);
    }

    private void checkPlayerEnemyCollision(Player player, ArrayList<Zombie> zombies, ArrayList<Devil> devils, ArrayList<ZombieDog> zombieDogs) {
        Rectangle playerRect = player.getRec();
        checkPlayerWithEnemies(player, playerRect, zombies);
        checkPlayerWithEnemies(player, playerRect, devils);
        checkPlayerWithEnemies(player, playerRect, zombieDogs);
    }

    private <T extends GameObject> void checkPlayerWithEnemies(Player player, Rectangle playerRect, ArrayList<T> enemies) {
        for (T enemy : enemies) {
            if (playerRect.intersects(enemy.getRec())) {
                playerEnemyCollision(enemy, player);
            }
        }
    }

    private void checkEnemyCollisions(ArrayList<Zombie> zombies, ArrayList<Devil> devils, ArrayList<ZombieDog> zombieDogs) {
        checkSameTypeCollisions(zombies);
        checkSameTypeCollisions(devils);
        checkSameTypeCollisions(zombieDogs);
        checkDifferentTypeCollisions(zombies, devils);
        checkDifferentTypeCollisions(zombies, zombieDogs);
        checkDifferentTypeCollisions(devils, zombieDogs);
    }

    private <T extends GameObject> void checkSameTypeCollisions(ArrayList<T> enemies) {
        for (int i = 0; i < enemies.size(); i++) {
            T enemy1 = enemies.get(i);
            for (int j = i + 1; j < enemies.size(); j++) {
                T enemy2 = enemies.get(j);
                if (enemy1.getRec().intersects(enemy2.getRec())) {
                    enemyCollision(enemy1, enemy2);
                }
            }
        }
    }

    private <T extends GameObject, U extends GameObject> void checkDifferentTypeCollisions(ArrayList<T> enemies1, ArrayList<U> enemies2) {
        for (T enemy1 : enemies1) {
            for (U enemy2 : enemies2) {
                if (enemy1.getRec().intersects(enemy2.getRec())) {
                    enemyCollision(enemy1, enemy2);
                }
            }
        }
    }

    private void playerEnemyCollision(GameObject enemy, Player player) {
        if (enemy.getX() < player.getX()) {
            enemy.setX(enemy.getX() - 1);
        } else {
            enemy.setX(enemy.getX() + 1);
        }
        if (enemy.getY() < player.getY()) {
            enemy.setY(enemy.getY() - 1);
        } else {
            enemy.setY(enemy.getY() + 1);
        }
    }

    private void enemyCollision(GameObject enemy1, GameObject enemy2) {
        Rectangle enemy1Rect = enemy1.getRec();
        Rectangle enemy2Rect = enemy2.getRec();

        if (enemy1Rect.intersects(enemy2Rect)) {
            // 确定两个敌人之间的碰撞方向
            int dx = enemy2Rect.x - enemy1Rect.x;
            int dy = enemy2Rect.y - enemy1Rect.y;

            // 将敌人推开一定的距离，确保它们不会重叠
            if (Math.abs(dx) > Math.abs(dy)) {
                if (dx > 0) {
                    enemy1.setX(enemy1.getX() - 1);
                    enemy2.setX(enemy2.getX() + 1);
                } else {
                    enemy1.setX(enemy1.getX() + 1);
                    enemy2.setX(enemy2.getX() - 1);
                }
            } else {
                if (dy > 0) {
                    enemy1.setY(enemy1.getY() - 1);
                    enemy2.setY(enemy2.getY() + 1);
                } else {
                    enemy1.setY(enemy1.getY() + 1);
                    enemy2.setY(enemy2.getY() - 1);
                }
            }

            // 确保敌人不会被推到墙体内
            if (checkCollisionWithMap(enemy1)) {
                enemy1.setX(enemy1.getX() - dx);
                enemy1.setY(enemy1.getY() - dy);
            }

            if (checkCollisionWithMap(enemy2)) {
                enemy2.setX(enemy2.getX() + dx);
                enemy2.setY(enemy2.getY() + dy);
            }
        }
    }


    private void checkPlayerEnemyDamage(Player player, ArrayList<Zombie> zombies, ArrayList<Devil> devils, ArrayList<ZombieDog> zombieDogs) {
        Rectangle playerRect = player.getRec();
        checkPlayerWithEnemiesForDamage(player, playerRect, zombies);
        checkPlayerWithEnemiesForDamage(player, playerRect, devils);
        checkPlayerWithEnemiesForDamage(player, playerRect, zombieDogs);
    }

    private <T extends GameObject> void checkPlayerWithEnemiesForDamage(Player player, Rectangle playerRect, ArrayList<T> enemies) {
        for (T enemy : enemies) {
            if (playerRect.intersects(enemy.getRec())) {
                playerTakeDamage(player, enemy);
            }
        }
    }

    private void checkPlayerBulletCollision(Player player, ArrayList<Zombie> zombies, ArrayList<Devil> devils, ArrayList<ZombieDog> zombieDogs) {
        List<Bullets> bullets = player.getCurrentWeapon().getBullets();
        for (Bullets bullet : bullets) {
            Rectangle bulletRect = bullet.getRec();
            checkBulletWithEnemies(bullet, bulletRect, zombies);
            checkBulletWithEnemies(bullet, bulletRect, devils);
            checkBulletWithEnemies(bullet, bulletRect, zombieDogs);
        }
    }

    private <T extends GameObject> void checkBulletWithEnemies(Bullets bullet, Rectangle bulletRect, ArrayList<T> enemies) {
        for (T enemy : enemies) {
            if (bulletRect.intersects(enemy.getRec()) && !bullet.getHitEnemies().contains(enemy)) {
                bullet.getHitEnemies().add(enemy); // 添加到击中的敌人列表
                enemyTakeDamage(bullet, enemy);
                bullet.setPenetration(bullet.getPenetration() - 1);
                if (bullet.getPenetration() <= 0) {
                    bullet.setPenetrated(true); // 设置子弹穿透完后移除
                    break;
                }
            }
        }
    }

    private void checkDevilBulletCollision(Player player, ArrayList<Devil> devils) {
        Rectangle playerRect = player.getRec();
        for (Devil devil : devils) {
            List<DevillBullets> devilBullets = devil.getBullets();
            for (DevillBullets bullet : devilBullets) {
                if (playerRect.intersects(bullet.getRec())) {
                    playerTakeDamageByBullets(player, bullet);
                }
            }
        }
    }

    private void playerTakeDamage(Player player, GameObject enemy) {
        player.takeDamage(1);
        //System.out.println("Player is attacked");
        Sound.playerHurtSound1.playSoundEffect();
    }

    private void enemyTakeDamage(Bullets bullet, GameObject enemy) {
        int damage = bullet.getDamage();
        if (enemy instanceof Zombie) {
            ((Zombie) enemy).takeDamage(damage);
        } else if (enemy instanceof Devil) {
            ((Devil) enemy).takeDamage(damage);
        } else if (enemy instanceof ZombieDog) {
            ((ZombieDog) enemy).takeDamage(damage);
        }
        //System.out.println("Bullet hit enemy!");
        //bulletImpactBody.play();
    }

    private void playerTakeDamageByBullets(Player player, DevillBullets bullet) {
        player.takeDamage(5);
        //System.out.println("Enemy bullet hit player!");
        Sound.playerHurtSound2.playSoundEffect();
    }
}
