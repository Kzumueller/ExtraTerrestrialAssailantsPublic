package com.zumueller.extraterrestrialassailants.actors

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import kotlin.math.max

/** base class for all actors switching between two images to signify movement or fill it with life */
open class SpriteWithTwoImages(region1: TextureRegion, region2: TextureRegion) : Group() {
  var image1 = Image(region1)
  var image2 = Image(region2)
  var vector = Vector2()
  var rectangle1 = Rectangle()
  var rectangle2 = Rectangle()

  /** action to show image1 and hide image2 */
  val visible1 = object : Action() {
    override fun act(delta: Float): Boolean {
      image1.isVisible = true
      image2.isVisible = false

      return true
    }
  }

  /** opposite of visible1 */
  val visible2 = object : Action() {
    override fun act(delta: Float): Boolean {
      image1.isVisible = false
      image2.isVisible = true

      return true
    }
  }

  init {
    this.addActor(image1)
    this.addActor(image2)
    image2.isVisible = false

    this.setSize(
      max(image1.width, image2.width),
      max(image1.height, image2.height)
    )

    rectangle1 = Rectangle(0f, 0f, width, height)
  }

  /** basic collision detection using rectangles for extending classes to use */
  fun checkCollision(actor: Actor): Boolean {
    actor.localToStageCoordinates(vector.set(0f, 0f))
    stageToLocalCoordinates(vector)
    rectangle2.set(vector.x, vector.y, actor.width, actor.height)

    return rectangle2.overlaps(rectangle1)
  }
}