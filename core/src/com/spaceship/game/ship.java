package com.spaceship.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class ship {
    public Vector2 position;
    public Vector2 velocity;
    private Texture texture;
    public float rotation;
    private float width;
    private float height;

    public ship(Texture texture) {
        this.texture = texture;
        this.position = new Vector2();
        this.velocity = new Vector2();
        // Manually specify dimensions
        this.width = 64;  // Adjust as needed
        this.height = 64; // Adjust as needed
    }

    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), 28, 35);
    }

    public Texture getTexture() {
        return texture;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void update(float deltaTime, float screenWidth, float screenHeight) {
        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;
    
        if (position.x < -width) position.x = screenWidth + width;
        if (position.x > screenWidth + width) position.x = -width;
        if (position.y < -height) position.y = screenHeight + height;
        if (position.y > screenHeight + height) position.y = -height;
    }

    public void updateRotation() {
        if (velocity.x != 0 || velocity.y != 0) {
            rotation = (float) Math.atan2(velocity.y, velocity.x) * 180 / (float) Math.PI;
        }
    }

    public float getRotation() {
        return rotation;
    }

    public float getSpeed() {
        return 300; // Adjust as needed
    }
}
