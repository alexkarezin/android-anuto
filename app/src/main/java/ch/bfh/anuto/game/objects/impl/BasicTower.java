package ch.bfh.anuto.game.objects.impl;

import android.content.res.Resources;
import android.graphics.Canvas;

import ch.bfh.anuto.R;
import ch.bfh.anuto.game.Sprite;
import ch.bfh.anuto.game.objects.AimingTower;
import ch.bfh.anuto.util.math.Vector2;

public class BasicTower extends AimingTower {

    private final static float RELOAD_TIME = 0.2f;
    private final static float RANGE = 5f;

    private float mAngle;
    private Sprite mSprite;

    public BasicTower() {
        mRange = RANGE;
        mReloadTime = RELOAD_TIME;
    }

    public BasicTower(Vector2 position) {
        this();
        setPosition(position);
    }

    @Override
    public void init(Resources res) {
        super.init(res);

        mSprite = Sprite.fromResources(this, res, R.drawable.basic_tower);
        mSprite.calcMatrix(null, 1f, new Vector2(0.5f, 0.5f));
        mGame.addDrawObject(mSprite, LAYER);
    }

    @Override
    public void clean() {
        super.clean();

        mGame.removeDrawObject(mSprite);
    }

    @Override
    public void onBeforeDraw(Sprite sprite, Canvas canvas) {
        super.onBeforeDraw(sprite, canvas);
        canvas.rotate(mAngle);
    }

    @Override
    public void tick() {
        super.tick();

        if (mTarget != null) {
            if (mReloaded) {
                shoot(new BasicShot(mPosition, mTarget));
            }

            mAngle = getAngleTo(mTarget);
        }
    }
}
