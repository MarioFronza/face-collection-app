package com.mariofronza.face_collection_app.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment
import com.mariofronza.face_collection_app.ui.user.SignInActivity

class AppIntroActivity : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addSlide(
            AppIntroFragment.newInstance(
                title = "Olá",
                description = "Bem-vindo ao coletor de faces do Open Face Recognition System"
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                title = "O que é esse app? 🧐",
                description = "Este app tem o objetivo de coletar fotos de rostos para o treinamento dos algoritmos de reconhecimento facial existentes."
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                title = "O que você precisa fazer?",
                description = "Serão coletadas 30 fotos do seu rosto" +
                        "\n - 6 fotos de sua face normal 😐" +
                        "\n - 6 fotos de você sorrindo 😁" +
                        "\n - 6 fotos de olhos fechados 😑" +
                        "\n - 6 fotos do lado esquerdo 👈" +
                        "\n - 6 fotos do lado direito 👉"
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                title = "Quase lá",
                description = "Se vc chegou até aqui, provavelmente já tem um cadastro na aplicação, sendo seu e-mail e uma senha padrão. Você pode alterar ela quando entrar no app."
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                title = "Mais uma coisa",
                description = "Ao utilizar este app, vc concorda em disponibilizar as fotos de seu rosto para o desenvolvimento deste trabalho." +
                        "\n Suas fotos não serão compartilhadas com ninguém."
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