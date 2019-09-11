package com.zumueller.extraterrestrialassailants.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.zumueller.extraterrestrialassailants.sounds.SoundPlayer
import com.zumueller.extraterrestrialassailants.ui.MessageLabel
import kotlin.math.max
import kotlin.math.min

class Ship(region1: TextureRegion, region2: TextureRegion, projectileRegion: TextureRegion) : SpriteWithTwoImages(region1, region2) {

  val projectile = Image(projectileRegion)

  val explosion = PooledExplosionActor()

  var lives = Lives(region1, 3)

  var scoreLabel = Label("00000", Label.LabelStyle(BitmapFont(), Color(.7f, .2f, .3f, 1f)))

  var messageLabel = MessageLabel(Label.LabelStyle(BitmapFont(), Color.WHITE))

  init {
    scoreLabel.setPosition(435f, 0f)
    addAction(Actions.run {
      stage.addActor(lives)
      stage.addActor(scoreLabel)
      stage.addActor(messageLabel)
    })
  }

  /**
   * fires its projectile if it's yet flying, meaning there can only be one shot active at a time
   */
  fun shoot() {
    if (projectile.hasParent() || !isVisible) return

    SoundPlayer.play("shoot.wav", x + width / 2f)
    stage?.addActor(projectile)
    projectile.setPosition(x + width / 2f, y + height)
    projectile.addAction(Actions.sequence(
      Actions.moveBy(0f, Gdx.graphics.height.toFloat(), 1.5f),
      Actions.removeActor()
    ))
  }

  /** moves 4px to the left without leaving the screen */
  fun moveLeft() {
    move(max(-4f, -x))
  }

  /** moves 4px to the right without leaving the screen */
  fun moveRight() {
    val distanceToRightEdge = Gdx.graphics.width - (x + width)
    move(min(4f, distanceToRightEdge))
  }

  /** moves by the given distances, switching sprites before and after movement */
  private fun move(x: Float, y: Float = 0f) {
    addAction(
      Actions.sequence(
        visible2,
        Actions.moveBy(x, y),
        visible1)
    )
  }

  /** sets up an action controlling ... most of the game actually */
  fun setCollisionGroup(group: EnemyGroup) {
    addAction(Actions.forever(
      Actions.run {
        // here we look for collisions with the phalanx
        if (projectile.hasParent() && group.checkCollision(projectile)) {
          projectile.clearActions()
          projectile.remove()

          // every kill is worth 50 points
          val score = scoreLabel.text.toString().toInt() + 50
          scoreLabel.setText(score.toString().padStart(5, '0'))

          // What's the price of a life, you ask?
          if(0 == score % 1000) ++lives

          // Killing all the bad guys wins you the game
          // yet violence begets even more violence
          if(0 == group.children.size) {
            messageLabel.show("You Win!")
            resetGame(group)
          }
        }

        // checking collisions with the group of enemies and their projectiles
        if (isVisible && (group.checkCollision(this@Ship) || group.checkCollisionProjectileWithShip(this@Ship))) {
          SoundPlayer.play("explosion.wav", x + width / 2f)
          stage.addActor(explosion)
          explosion.start(x + width / 2f, y + height / 2f)

          if(0 > (--lives).lives) {
            gameOver(group)
          }

          // after every non-lethal hit, the ship will take a short time-out
          addAction(Actions.sequence(
            Actions.hide(),
            Actions.delay(3f),
            Actions.show()
          ))
        }

        if(group.reachedBottom) {
          gameOver(group)
        }
      }
    ))
  }

  fun gameOver (group: EnemyGroup) {
    messageLabel.show("Game Over")
    resetGame(group)
  }

  fun resetGame(group: EnemyGroup) {
    addAction(Actions.sequence(
      Actions.delay(4f),
      Actions.run {
        scoreLabel.setText("00000")
        lives.lives = 3
        group.reset()
      }
    ))
  }
}