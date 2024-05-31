package com.spaceship.game;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class meteor {
    public Vector2 position;
    public Vector2 velocity;
    public Texture texture;
    private float width;
    private float height;
    private float sizeFactor;
    private float rotation;
    private float rotationSpeed;


    public meteor(Texture texture) {
        this.texture = texture;
        this.position = new Vector2();
        this.velocity = new Vector2();
        // Manually specify dimensions
        this.width = 32;  // Adjust as needed
        this.height = 32; // Adjust as needed

        Random rand = new Random();
        this.sizeFactor = 0.5f + rand.nextFloat() * 1.5f;
        this.width *= sizeFactor;
        this.height *= sizeFactor;

        this.rotation = 0;
        this.rotationSpeed = (float) Math.random() * 360 - 180;
    }

    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), 25, 25);
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

    public void setSpeed(float speed) {
        float angle = velocity.angleRad();
        velocity.set((float) (speed * Math.cos(angle)), (float) (speed * Math.sin(angle)));
    }

    public void init(float screenWidth, float screenHeight) {
        float randomX = (float) Math.random() * screenWidth;
        float randomY = (float) Math.random() * 2 * screenHeight - screenHeight;
        position.set(randomX, randomY);

        float randomAngle = (float) Math.random() * (float) Math.PI * 2;
        float randomSpeed = 100 + (float) Math.random() * 200;
        velocity.set((float) Math.cos(randomAngle) * randomSpeed, (float) Math.sin(randomAngle) * randomSpeed);
    }

    public void update(float deltaTime, float screenWidth, float screenHeight) {
        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;

        if (position.x < -width) position.x = screenWidth + width;
        if (position.x > screenWidth + width) position.x = -width;
        if (position.y < -height) position.y = screenHeight + height;
        if (position.y > screenHeight + height) position.y = -height;

        updateRotation();

        rotation += rotationSpeed * deltaTime;
    }

    public void updateRotation() {
        if (velocity.x != 0 || velocity.y != 0) {
            rotation = (float) Math.atan2(velocity.y, velocity.x) * 180 / (float) Math.PI;
        }
    }

    public float getRotation() {
        return rotation;
    }
}
