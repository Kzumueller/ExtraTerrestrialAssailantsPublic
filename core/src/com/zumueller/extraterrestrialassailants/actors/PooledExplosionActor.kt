package com.zumueller.extraterrestrialassailants.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.scenes.scene2d.Actor
import com.zumueller.extraterrestrialassailants.particles.PooledExplosion

class PooledExplosionActor : Actor() {

  private lateinit var effect: ParticleEffect

  // let the
  fun start(x: Float, y: Float) {
    effect = PooledExplosion.getEffect()
    effect.setPosition(x, y)
    effect.start()
  }

  /** updates the held effect and returns it to the pool once complete */
  override fun act(delta: Float) {
    super.act(delta)
    effect.update(delta)

    if (effect.isComplete) {
      remove()
      PooledExplosion.free(effect)
    }
  }

  /** has the held effect draw itself */
  override fun draw(batch: Batch?, parentAlpha: Float) = effect.draw(batch)
}