package com.zumueller.extraterrestrialassailants.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.zumueller.extraterrestrialassailants.sounds.SoundPlayer

class Enemy(region1: TextureRegion, region2: TextureRegion, projectileRegion: TextureRegion) : SpriteWithTwoImages(region1, region2) {

  val projectile = Image(projectileRegion)

  init {
    addAction(Actions.forever(
      Actions.sequence(
        Actions.delay(.5f),
        visible2,
        Actions.delay(.5f),
        visible1)
      ))
  }

  fun shoot() {
    if(projectile.hasParent()) return

    SoundPlayer.play("shoot.wav", x + width / 2f)
    stage?.addActor(projectile)
    localToStageCoordinates(vector.set(width/2f, 0f))
    projectile.setPosition(vector.x, vector.y)

    projectile.addAction(Actions.sequence(
      Actions.moveBy(0f, -Gdx.graphics.height.toFloat(), 4f),
      Actions.removeActor()
    ))
  }

  fun checkCollisionProjectileWithShip(ship: Ship): Boolean {
    if(projectile.hasParent() && ship.checkCollision(projectile)) {
      projectile.clearActions()
      projectile.remove()

      return true
    }

    return false
  }

  fun EXPLOOODE() {
    val explosion = PooledExplosionActor()
    stage.addActor(explosion)
    vector.set(width/2f, height/2f)
    localToStageCoordinates(vector)
    explosion.start(vector.x, vector.y)
    SoundPlayer.play("explosion.wav", x + width / 2f)
    remove()
  }
}