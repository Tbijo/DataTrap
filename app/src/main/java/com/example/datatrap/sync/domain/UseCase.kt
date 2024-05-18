package com.example.datatrap.sync.domain

class GetCoinsUseCase(
    //private val repository: CoinRepository // inject the Interface then it is easily replaceable
) {
    // use cases should have one public function
    // and that is the function to execute that use case
    // in this case to get the Coins

    //1. we are overriding the invoke function we can call GetCoinsUseCase as it was a function
    //2. we return a Flow because we want to emit multiple values at a time
    // first we emit that we are Loading the data -> to display a progress bar
    // when it is Succesful we want to emit our data List of Coins
    // if it is a Error we want to emit the Error
    // These three states will be the Resource
//    operator fun invoke(): Flow<Resource<List<String>>> = flow {
//        try {
//            // First there needs to be loading to display progressBar
//            emit(Resource.Loading<List<String>>())
//            // dalej volame data a transform z Dto na Coin
//            val coins = repository.getCoins().map { it.toCoin() }
//            // ak nepride chyba mozeme poslat data
//            emit(Resource.Success<List<String>>(coins))
//        } catch (e: HttpException) {
//            // response code does not start with 2
//            emit(Resource.Error<List<String>>(e.localizedMessage ?: "Unexpected Error has occured"))
//        } catch (e: IOException) {
//            // API has no connection to Remote Data
//            emit(Resource.Error<List<String>>("Could not reach server, check internet connection"))
//        }
//    }
}