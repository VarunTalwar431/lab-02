package com.example.listcity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listcity.ui.theme.ListCityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val cityRepository = CityRepository()
        setContent {
            ListCityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CityListScreen(
                        cities = cityRepository.cities,
                        onAddCity = { cityRepository.addCity(it) },
                        onDeleteCity = {cityRepository.DeleteCity(it)},
                        modifier  = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

class CityRepository{
    // val means variable cannot be reassigned but contents of this list can change
    private val _cities = mutableStateListOf( // this is  a jet compose list
        "Edmonton","Vancouver","Moscow",
        "Sydney", "Berlin", "Vienna",
        "Tokyo", "Beijing", "Osaka",
        "New Delhi"
    )
// this creates a public read only view of list
    val cities: List<String>
        get() = _cities

    fun addCity(city:String){
        _cities.add(city)
    }


    fun DeleteCity(city:String){
        _cities.remove(city)
    }
}

// for the delete part we can use remove

@Composable
fun CityListScreen (cities:List<String>,
                    onAddCity: (String) ->Unit,
                    onDeleteCity: (String) -> Unit,
                    modifier: Modifier= Modifier) {
    // three states are handled here
    var newCityName by remember { mutableStateOf("")}
    var selectedCity by remember { mutableStateOf("") }
    var addingCity by remember {mutableStateOf(false)}
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        Spacer(modifier = Modifier.height(50.dp))
        Row(modifier = Modifier.padding(16.dp)){
            Button(//delete button
                onClick ={
                    onDeleteCity(selectedCity)
                    selectedCity =""
                }
            ){
                Text(text = "DELETE CITY")
            }
//            OutlinedTextField(
//                value = newCityName,
//                onValueChange = {newCityName =it},
//                label = {Text(text = "City Name")},
//                modifier = Modifier.weight(1f)
//            )
            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick={
                    addingCity = true
                }
            ){
                Text(text = "ADD CITY")
            }
        }
        LazyColumn(
            modifier = Modifier
                .padding(vertical =1.dp)
                .height(600.dp)
        ){
            items(cities){city->
                CityRow(city = city,
                    onClickedCity ={selectedCity = it})
            }
        }

        Row(

            modifier = Modifier.fillMaxWidth()
                        .padding(8.dp) ,
            verticalAlignment = Alignment.CenterVertically
        ){
            OutlinedTextField(
                value = newCityName,
                onValueChange = {newCityName =it},
                label = {Text(text = "City Name")},
                modifier = Modifier.weight(1f),
                enabled = addingCity
            )
            Spacer(modifier = Modifier.width(5.dp))
            
            Button(
                onClick = {
                    if (newCityName.isNotBlank()){
                        onAddCity(newCityName)
                        //newCityName = ""
                    }
                    Toast.makeText(
                        context,
                        "$newCityName has been Added to the list ",
                        Toast.LENGTH_LONG).show()
                        addingCity = false
                        newCityName = ""


                },
                modifier = Modifier.weight(1f)
            ){
                Text(text = "CONFIRM")
            }
        }


    }

}

@Composable
fun CityRow(city:String, onClickedCity:(String) -> Unit){
    Text(text = city,
        fontSize = 28.sp,
        modifier = Modifier.clickable{
            onClickedCity(city)
        }
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 14.dp)
    )
}
