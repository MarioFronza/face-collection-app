package com.mariofronza.face_collection_app.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroCustomLayoutFragment
import com.github.appintro.AppIntroFragment
import com.mariofronza.face_collection_app.R
import com.mariofronza.face_collection_app.ui.user.SignInActivity

class AppIntroActivity : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addSlide(
            AppIntroFragment.newInstance(
                title = "Olá",
                description = "Bem-vindo ao coletor de faces do Open Face Recognition System."
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                title = "O que é esse app? 🧐",
                description = "Este app tem o objetivo de coletar fotos de rostos para o treinamento dos algoritmos de reconhecimento existentes."
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                title = "O que você precisa fazer?",
                description = "Serão coletadas através do app 30 fotos do seu rosto" +
                        "\n - 6 fotos de sua face normal 😐" +
                        "\n - 6 fotos de você sorrindo 😁" +
                        "\n - 6 fotos de olhos fechados 😑" +
                        "\n - 6 fotos do lado esquerdo 👈" +
                        "\n - 6 fotos do lado direito 👉"
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                title = "Uma dica",
                description = "Tente variar um pouco suas expressões faciais em cada foto. Se você usa óculos 👓, varie a cada foto usando e não usando seus óculos."
            )
        )
        addSlide(
            AppIntroCustomLayoutFragment.newInstance(
                R.layout.term
            )
        )

    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        goToSignInActivity()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        goToSignInActivity()
    }

    private fun goToSignInActivity() {
        Intent(this, SignInActivity::class.java).also {
            startActivity(it)
        }
        finish()
    }
}