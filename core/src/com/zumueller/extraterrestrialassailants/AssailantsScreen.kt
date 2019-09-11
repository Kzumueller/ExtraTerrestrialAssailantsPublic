package com.zumueller.extraterrestrialassailants

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.zumueller.extraterrestrialassailants.actors.EnemyGroup
import com.zumueller.extraterrestrialassailants.actors.Particle
import com.zumueller.extraterrestrialassailants.actors.Ship

class AssailantsScreen : ScreenAdapter() {
  val textureAtlas = TextureAtlas(Gdx.files.internal("sprites-packed/pack.atlas"))

  val phalanx = EnemyGroup(textureAtlas, 6, 6)

  val ship = Ship(
    textureAtlas.findRegion("ship1"),
    textureAtlas.findRegion("ship2"),
    textureAtlas.findRegion("shoot")
  )

  val stage = Stage()

  var width = Gdx.graphics.width
  var height = Gdx.graphics.height

  override fun show() {
    ship.setPosition(width / 2f, 20f)
    ship.setCollisionGroup(phalanx)

    ship.addAction(Actions.forever(
      Actions.run {
        if(Gdx.input.isKeyPressed(Input.Keys.A))
          ship.moveLeft()

        if(Gdx.input.isKeyPressed(Input.Keys.D))
          ship.moveRight()

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE))
          ship.shoot()
      }
    ))

    val stars = Particle("stars")
    stars.start(Gdx.graphics.width.toFloat() * .5f, 0f)

    stage.addActor(stars)
    stage.addActor(phalanx)
    stage.addActor(ship)
  }

  override fun render(delta: Float) {
    Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT)
    Gdx.gl.glClearColor(0f, 0f, 0f, 1f)

    stage.act(delta)
    stage.draw()
  }

  override fun resize(width: Int, height: Int) {
    this.width = width
    this.height = height
  }
}