package com.zumueller.extraterrestrialassailants.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label

class MessageLabel(style: LabelStyle) : Label("Test", style) {
  val layout = GlyphLayout()

  init {
    style.font.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
    style.font.data.setScale(3f)
    addAction(Actions.alpha(0f))
  }

  fun show(message: String) {
    setText(message)
    val screenWidth = Gdx.graphics.width
    val screenHeight = Gdx.graphics.height
    layout.setText(style.font, message)

    setPosition(.5f * (screenWidth - layout.width), .5f * (screenHeight - layout.height))

    addAction(Actions.sequence(
      Actions.fadeIn(1f),
      Actions.delay(2f),
      Actions.fadeOut(1f)
    ))
  }
}