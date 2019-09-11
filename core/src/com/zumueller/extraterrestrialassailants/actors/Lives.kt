package com.zumueller.extraterrestrialassailants.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor

class Lives(var region: TextureRegion, var lives: Int) : Actor() {

  val ratio : Float = region.regionHeight.toFloat() / region.regionWidth.toFloat()

  /** draws a tiny sprite for every life the player has */
  override fun draw(batch: Batch?, parentAlpha: Float) {
    super.draw(batch, parentAlpha)

    for(index in 0 until lives) {
      batch?.draw(region, 5f + 33f * index, 2f, 30f, 30 * ratio)
    }
  }

  /** literal fun with operators */

  operator fun dec(): Lives {
    --lives

    return this
  }

  operator fun inc(): Lives {
    ++lives

    return this
  }
}