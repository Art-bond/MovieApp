package ru.d3st.academyandroid.database

import ru.d3st.academyandroid.R
import ru.d3st.academyandroid.domain.Movie

object Data {

    private val actors = listOf(
        Movie.Actor(
            "Robert Downey",
            R.drawable.movie11
        ),
        Movie.Actor(
            "Chris Evans",
            R.drawable.movie12
        ),
        Movie.Actor(
            "Mark Ruffalo",
            R.drawable.movie13
        ),
        Movie.Actor(
            "Chris Hemsworth",
            R.drawable.movie14
        )
    )

    // наша база фильмов
    val movie1 = Movie(
        "Avengers: End Game",
        R.drawable.orig1,
        R.drawable.movie_back1,
        "Action, Adventure, Fantasy",
        4,
        "127 Reviews",
        "After the devastating events of Avengers: Infinity War, the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos actions and restore balance to the universe.",
        "137 min",
        "13+",
        actors
    )
    private val actors2 = listOf(
        Movie.Actor(
            "John David Washington",
            R.drawable.movie21
        ),
        Movie.Actor(
            "Martin Donovan",
            R.drawable.movie22
        ),
        Movie.Actor(
            "Clémence Poésy",
            R.drawable.movie23
        ),
        Movie.Actor(
            "Robert Pattinson",
            R.drawable.movie24
        )
    )

    // наша база фильмов
    val movie2 = Movie(
        "Tenet",
        R.drawable.orig1,
        R.drawable.movie_back2,
        "Action, Sci-Fi",
        5,
        "98 Reviews",
        "Armed with only one word, Tenet, and fighting for the survival of the entire world, a Protagonist journeys through a twilight world of international espionage on a mission that will unfold in something beyond real time.",
        "97 min",
        "16+",
        actors2
    )
    private val actors3 = listOf(
        Movie.Actor(
            "Scarlett Johansson",
            R.drawable.movie31
        ),
        Movie.Actor(
            "Florence Pugh",
            R.drawable.movie32
        ),
        Movie.Actor(
            "Rachel Weisz",
            R.drawable.movie33
        ),
        Movie.Actor(
            "David Harbour",
            R.drawable.movie34
        )
    )

    // наша база фильмов
    val movie3 = Movie(
        "Black Widow",
        R.drawable.orig1,
        R.drawable.movie_back3,
        "Action, Advanture, Sci-Fi",
        4,
        "38 Reviews",
        "A film about Natasha Romanoff in her quests between the films Civil War and Infinity War.",
        "102 min",
        "13+",
        actors3
    )
    private val actors4 = listOf(
        Movie.Actor(
            "Gal Gadot",
            R.drawable.movie41
        ),
        Movie.Actor(
            "Pedro Pascal",
            R.drawable.movie42
        ),
        Movie.Actor(
            "Chris Pine",
            R.drawable.movie43
        ),
        Movie.Actor(
            "Kristen Wiig",
            R.drawable.movie44
        )
    )

    // наша база фильмов
    val movie4 = Movie(
        "Wonder Woman 1984",
        R.drawable.orig1,
        R.drawable.movie_back4,
        "Action, Sci-Fi",
        5,
        "74 Reviews",
        "Fast forward to the 1980s as Wonder Woman's next big screen adventure finds her facing two all-new foes: Max Lord and The Cheetah.",
        "120 min",
        "13+",
        actors4
    )


}