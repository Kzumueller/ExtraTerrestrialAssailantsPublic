package com.zumueller.extraterrestrialassailants.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import kotlin.math.max

class EnemyGroup(val atlas: TextureAtlas, val rows: Int, val columns: Int) : Group() {

  var vector = Vector2()
  var rectangle1 = Rectangle()
  var rectangle2 = Rectangle()
  var reachedBottom = false

  init {
    reset()
  }

  /**
   * registers movement actions: left to right, right to left, down one row
   * repeats this sequence with an ever lowering duration indefinitely
   */
  fun registerMovement(duration: Float) {
    val offset = Gdx.graphics.width - width

    addAction(Actions.sequence(
      Actions.moveBy(offset, 0f, duration),
      Actions.delay(.5f),
      Actions.moveBy(-offset, 0f, duration),
      Actions.delay(.5f),
      Actions.moveBy(0f, -40f, duration),
      Actions.delay(.5f),
      Actions.run {
        reachedBottom = 1 > y && 1 > y + minY()
        registerMovement(.75f * duration)
      }
    ))
  }

  /** sets up shooting in regular intervals */
  fun registerShooting() {
    addAction(Actions.sequence(
      Actions.delay(Math.random().toFloat() * 4f),
      Actions.run {
        shoot()
        registerShooting()
      }
    ))
  }

  /** pew pew? */
  fun shoot() = (children.random() as Enemy?)?.shoot()

  /** determines whether the given actor overlaps with the group before delegating collision detection to the individual members */
  fun checkCollision(actor: Actor): Boolean {
    actor.localToStageCoordinates(vector.set(0f, 0f))
    stageToLocalCoordinates(vector)
    rectangle2.set(vector.x, vector.y, actor.width, actor.height)

    if(!rectangle2.overlaps(rectangle1)) return false

    children.forEach {
      if((it as Enemy).checkCollision(actor)) {
        it.EXPLOOODE()

        return true
      }
    }

    return false
  }

  fun checkCollisionProjectileWithShip(ship: Ship): Boolean {
    children.forEach {
      if((it as Enemy).checkCollisionProjectileWithShip(ship))
        return true
    }

    return false
  }

  fun minY (): Float = children.minBy { it.y }?.y ?: Float.MAX_VALUE

  /** sets up a grid of enemies using the specified number of rows and columns */
  fun reset() {
    clear()
    clearActions()
    setPosition(0f, 500f)
    reachedBottom = false

    var imageIndex = 0
    var y = 0f
    var width = 0f
    var enemy: Enemy? = null

    for (row in 0 until rows) {
      var x = 0f

      for(column in 0 until columns) {
        val region1 = atlas.findRegion("e$imageIndex-0")
        val region2 = atlas.findRegion("e$imageIndex-1")
        imageIndex = ++imageIndex % 10

        enemy = Enemy(region1, region2, atlas.findRegion("shoot"))
        enemy.setPosition(x, y)
        addActor(enemy)
        x += enemy.width + 10f
      }

      width = max(x, width)
      y += (enemy?.height ?: 0f) + 10f
    }

    setSize(width, y)
    rectangle1 = Rectangle(0f, 0f, width, y)

    registerMovement(2f)
    registerShooting()
  }
}