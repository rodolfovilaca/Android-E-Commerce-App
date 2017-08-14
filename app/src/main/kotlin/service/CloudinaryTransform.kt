package service

import com.cloudinary.Cloudinary
import com.cloudinary.Transformation

class CloudinaryTransform {
    fun transform(): String{
        val cloud : Cloudinary =  Cloudinary(CloudinaryConfig().getConfig())
        val trasformation: Transformation = Transformation()
        trasformation.width(1000).height(200).radius(10)
        return cloud.url().transformation(trasformation).generate("camaraointernaciona_lfmdp1")
    }
}