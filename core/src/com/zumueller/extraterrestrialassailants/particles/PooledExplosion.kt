package com.zumueller.extraterrestrialassailants.particles

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.*

class PooledExplosion {
  companion object {
    private var pool: ParticleEffectPool

    init {
      val effect = ParticleEffect()
      effect.load(
        Gdx.files.internal("particles/explode"),
        Gdx.files.internal("particles/img")
      )

      pool = ParticleEffectPool(effect, 4, 37)
    }

    /** returns an effect from the static pool */
    @Synchronized
    fun getEffect(): PooledEffect = pool.obtain()

    /** frees a pooled effect for reuse */
    @Synchronized
    fun free(effect: ParticleEffect?) = pool.free(effect as PooledEffect)
  }
}