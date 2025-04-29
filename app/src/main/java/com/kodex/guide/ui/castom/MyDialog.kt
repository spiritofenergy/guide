package com.kodex.guide.ui.castom

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.google.android.play.core.integrity.m

@Composable
fun MyDialog(
   showDialog: Boolean,
   onDismiss: () -> Unit,
   onConfirm: () -> Unit,
   title: String = "Reset Password",
   massage: String = "",
   confirmButtonText: String = "Ok",
) {
   if (showDialog){
      AlertDialog(
         onDismissRequest = {
            onDismiss()
         },
         confirmButton = {
            Button(onClick = {
               onConfirm()
            }) {Text(text = confirmButtonText)
            }

            Button(onClick = {
               onDismiss()
            }) {Text(text = "Cansel")

            }
         },
         title = {
            Text(text = title,
               fontSize = 20.sp,
               color = Color.Black)
         },
         text={
            Text(
               text = massage,
               color= Color.Black,
               fontSize = 16.sp)

               }
            )
         }
      }
