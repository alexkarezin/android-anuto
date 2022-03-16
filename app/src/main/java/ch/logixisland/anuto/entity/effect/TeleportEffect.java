package ch.logixisland.anuto.entity.effect;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import ch.logixisland.anuto.engine.logic.GameEngine;
import ch.logixisland.anuto.engine.logic.entity.Entity;
import ch.logixisland.anuto.engine.render.Drawable;
import ch.logixisland.anuto.engine.render.Layers;
import ch.logixisland.anuto.entity.enemy.Enemy;
import ch.logixisland.anuto.util.math.Vector2;

public class TeleportEffect extends Effect implements Entity.Listener {

    private static final float EFFECT_DURATION = 1f;

    private static class StaticData {
        private Paint mPaint;
    }
    private class TeleportDrawable implements Drawable {

        public TeleportDrawable() {
        }

        @Override
        public int getLayer() {
            return Layers.SHOT;
        }

        @Override
        public void draw(Canvas canvas) {
            Vector2 target = mTarget.getPosition();
            canvas.drawLine(getPosition().x(), getPosition().y(), target.x(), target.y(), mStaticData.mPaint);
        }

    }

    private Enemy mTarget;

    private float mDistance;
    private Vector2 mMoveDirection;
    private float mMoveStep;
    private TeleportDrawable mDrawObject;
    private StaticData mStaticData;

    public TeleportEffect(Entity origin, Vector2 position, Enemy target, float distance) {
        super(origin, EFFECT_DURATION);
        setPosition(position);

        target.startTeleport();

        mTarget = target;
        mDistance = distance;
        mTarget.addListener(this);

        mMoveDirection = target.getDirectionTo(this);
        mMoveStep = target.getDistanceTo(this) / EFFECT_DURATION / GameEngine.TARGET_FRAME_RATE;

        mDrawObject = new TeleportDrawable();
    }

    @Override
    public Object initStatic() {
        return null;
    }

    @Override
    public void init() {
        super.init();
        mStaticData = (StaticData) getStaticData();
        getGameEngine().add(mDrawObject);
    }

    @Override
    public void clean() {
        super.clean();
        getGameEngine().remove(mDrawObject);
    }

    @Override
    public void tick() {
        super.tick();
        mTarget.move(Vector2.mul(mMoveDirection, mMoveStep));
    }

    @Override
    public void entityRemoved(Entity entity) {
        mTarget = null;
        remove();
    }

    @Override
    protected void effectEnd() {
        if (mTarget != null) {
            mTarget.sendBack(mDistance);
            mTarget.finishTeleport();
        }
    }
}
