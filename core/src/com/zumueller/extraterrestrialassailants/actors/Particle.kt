package com.zumueller.extraterrestrialassailants.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.scenes.scene2d.Actor

class Particle(name: String) : Actor() {
  val effect = ParticleEffect()

  init {
    effect.load(
      Gdx.files.internal("particles/$name"),
      Gdx.files.internal("particles/img")
    )
  }

  fun start(x: Float, y: Float) {
    effect.setPosition(x, y)
    effect.start()
  }

  override fun act(delta: Float) {
    super.act(delta)

    effect.update(delta)
    if(effect.isComplete) remove()
  }

  override fun draw(batch: Batch?, parentAlpha: Float) = effect.draw(batch)
}