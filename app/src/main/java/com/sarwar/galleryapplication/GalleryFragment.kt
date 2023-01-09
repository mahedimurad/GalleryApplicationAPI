package com.sarwar.galleryapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sarwar.galleryapplication.databinding.FragmentGalleryBinding
import com.sarwar.galleryapplication.model.ImageModel
import com.sarwar.galleryapplication.model.UnsplashApiResponse
import kotlinx.android.synthetic.main.item_image.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class GalleryFragment : Fragment() {

    private var pageSize: Int = 100
    private var page: Int = 0
    lateinit var binding: FragmentGalleryBinding
    private var photos = ArrayList<ImageModel>()
    private lateinit var retrofit: Retrofit
    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_gallery, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()

        retrofit = Retrofit.Builder()
                   .baseUrl("https://api.unsplash.com/")
                   .client(
                       OkHttpClient.Builder()
                           .addInterceptor { chain: Interceptor.Chain ->
                               val request = chain.request().newBuilder()
                                   .addHeader("Authorization","Client-ID GEWcXNfJeYOkEhStfk7EyVtCWgwd1Vnj2EozkIbpvfs")
                               chain.proceed(request.build())
                           }.build()
                   )
                   .addConverterFactory(GsonConverterFactory.create())
                   .build()


        apiService = retrofit.create(ApiService::class.java)


        searchImage()



        //Network call on unsplash api
        // Retrofit 2
        // ApiService Interface -> GET, POST, PUT, UPDATE - done
        // Json - done
        // POJO Class/ Data class/ Model -  done
        //


    }

    private fun searchImage() {
        apiService.searchImage("nature",page,pageSize)
            .enqueue(object : Callback<UnsplashApiResponse>{

                override fun onResponse(
                    call: Call<UnsplashApiResponse>,
                    response: Response<UnsplashApiResponse>
                ) {
                    response.body()?.let {
                        photos.addAll(it.results)
                        binding.rvGallery.adapter!!.notifyDataSetChanged()
                    }?: Toast.makeText(requireContext(),"body null",Toast.LENGTH_SHORT).show()

                }

                override fun onFailure(call: Call<UnsplashApiResponse>, t: Throwable) {
                    Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun prepareRecyclerView() {
        binding.rvGallery.setHasFixedSize(true)
        binding.rvGallery.layoutManager = GridLayoutManager(requireContext(),3)
        binding.rvGallery.adapter = PhotoAdapter(requireContext() , photos){

            findNavController().navigate(GalleryFragmentDirections.actionGalleryFragmentToPhotoViewerFragment(it.urls.small))

        }

        binding.rvGallery.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(!recyclerView.canScrollHorizontally(1)
                    && newState == RecyclerView.SCROLL_STATE_IDLE){
                    page++
                    searchImage()
                }
            }
        })
    }


}