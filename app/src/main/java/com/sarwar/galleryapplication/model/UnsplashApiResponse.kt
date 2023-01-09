package com.sarwar.galleryapplication.model

data class UnsplashApiResponse(
     val total: Int,
     val total_pages:Int,
     val results:ArrayList<ImageModel>
)