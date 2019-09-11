package com.zumueller.extraterrestrialassailants.sounds

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound

class SoundPlayer {
  companion object {
    val sounds = HashMap<String, Sound>()

    init {
      // for a directory name (i.e. containing all sounds),
      // Gdx.files.internal will always return a file handle whose method "list" will return an empty array
      // for directories on the classpath
      arrayOf("shoot.wav", "explosion.wav").forEach {
        sounds[it] = Gdx.audio.newSound(Gdx.files.internal("sounds/$it"))
      }
    }

    /** plays a sound with the given name, moved across the stereo panorama using horizontal */
    @Synchronized
    fun play(name: String, horizontal: Float = 0f) {
      val panorama = 2f * horizontal / Gdx.graphics.width.toFloat() - 1f

      sounds[name]?.play(1f, 1f, panorama)
    }
  }
}