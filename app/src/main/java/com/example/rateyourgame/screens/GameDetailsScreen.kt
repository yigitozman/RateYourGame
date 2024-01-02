package com.example.rateyourgame.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.rateyourgame.R
import com.example.rateyourgame.ViewModels.AuthViewModel
import com.example.rateyourgame.ViewModels.RatingViewModel
import com.example.rateyourgame.dataclasses.Game
import com.example.rateyourgame.instances.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun GameDetailsScreen(authViewModel: AuthViewModel, ratingViewModel: RatingViewModel, gameId: Int) {
    var game by remember { mutableStateOf<Game?>(null) }
    val apiKey = "7e45a963d9924c2cb094208bddb962b3"
    val user by authViewModel.user.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var averageRating by remember { mutableStateOf<Float?>(null) }
    var ratingCount by remember { mutableStateOf<Int>(0) }
    var userRating by remember { mutableStateOf<Int?>(null) }
    var isReviewSubmitted by remember { mutableStateOf(false) }



    LaunchedEffect(apiKey) {
        val fetchedGame = fetchDataById(gameId, apiKey)
        game = fetchedGame
    }

    LaunchedEffect(ratingViewModel) {
        userRating = ratingViewModel.getRatingScoreByUserIdAndGameId(user?.id, gameId)
        averageRating = ratingViewModel.getAverageRatingByGameId(gameId)
        ratingCount = ratingViewModel.getRatingCount(gameId)
    }

    LaunchedEffect(ratingViewModel) {
        ratingViewModel.getAverageRatingByGameId(gameId)
    }

    game?.let { game ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .verticalScroll(state = scrollState)
                .fillMaxWidth()

        )
        {
            Box(modifier = Modifier, contentAlignment = Alignment.BottomEnd){
                AsyncImage(
                    model = game.background_image,
                    placeholder = painterResource(id = R.drawable.placeholder),
                    error = painterResource(id = R.drawable.placeholder),
                    contentDescription = "Game Logo",
                )

            }
            Spacer(modifier = Modifier.height(3.dp))
            Column(
                modifier = Modifier
                    .padding(20.dp)
            )
            {
                Text(text = game.name, fontSize = 26.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Metacritic Score", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                Text(text = game.metacritic, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Description", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = game.description_raw, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(20.dp))

                if (averageRating != null) {
                    Text("Rating Count: $ratingCount", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                    Text("Average Rating: $averageRating", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                } else {
                    Text("No review available..", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                }

                var currentRating by remember { mutableStateOf(0) }

                if (userRating != null) {
                    RatingBar(
                        maxRating = 5,
                        initialRating = userRating!!,
                        onRatingChanged = { newRating ->
                            userRating = newRating
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        if (userRating!! > 0) {
                            Button(onClick = {
                                coroutineScope.launch {
                                    ratingViewModel.insertOrUpdateRow(
                                        user?.id,
                                        gameId,
                                        userRating!!
                                    )
                                }
                                isReviewSubmitted = true
                            }) {
                                Text("Change Your Submission ${user?.username}")
                            }
                        }
                    }
                }
                else {
                    RatingBar(
                        maxRating = 5,
                        initialRating = currentRating,
                        onRatingChanged = { newRating ->
                            currentRating = newRating
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        if (currentRating > 0) {
                            Button(onClick = {
                                coroutineScope.launch {
                                    ratingViewModel.insertOrUpdateRow(
                                        user?.id,
                                        gameId,
                                        currentRating
                                    )
                                }
                                isReviewSubmitted = true
                            }) {
                                Text("Rate Your Game ${user?.username}")
                            }
                        }
                    }
                }

                if (isReviewSubmitted) {
                    AlertDialog(
                        onDismissRequest = {
                            isReviewSubmitted = false

                            coroutineScope.launch {
                                averageRating = ratingViewModel.getAverageRatingByGameId(gameId)
                                ratingCount = ratingViewModel.getRatingCount(gameId)
                            }
                        },
                        title = { Text("Thank You!") },
                        text = { Text("Your review has been submitted.") },
                        confirmButton = {
                            Button(onClick = {
                                isReviewSubmitted = false

                                coroutineScope.launch {
                                    averageRating = ratingViewModel.getAverageRatingByGameId(gameId)
                                    ratingCount = ratingViewModel.getRatingCount(gameId)
                                    userRating = ratingViewModel.getRatingScoreByUserIdAndGameId(user?.id, gameId)
                                }

                            }) {
                                Text("OK")
                            }
                        }
                    )
                }

            }
        }
    }
}
@Composable
fun RatingBar(
    maxRating: Int = 5,
    initialRating: Int = 0,
    onRatingChanged: (Int) -> Unit
) {
    var rating by remember { mutableStateOf(initialRating) }
    var filledstar= painterResource(id =R.drawable.filled_star )
    var outlinedstar= painterResource(id = R.drawable.empty_star)

    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,) {
        for (i in 1..maxRating) {
            Icon(
                painter = if (i <= rating) filledstar else outlinedstar,
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .padding(3.dp)
                    .clickable {
                        rating = i
                        onRatingChanged(i)
                    }
            )
        }
    }
}


suspend fun fetchDataById(gameId: Int, apiKey: String): Game? {
    return withContext(Dispatchers.IO) {
        val response = RetrofitInstance.rawgApi.getGameDetails(gameId, apiKey)
        if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}