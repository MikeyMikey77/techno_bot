package khpi.cs.se.db.semesterwork.cloudStorage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.pengrad.telegrambot.model.File;
import khpi.cs.se.db.semesterwork.bot.BotMarket;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

public class MyCloudStorage {

    public static String uploadPhoto(MultipartFile file, String title) throws Exception {
        Map<Object, Object> optionsCloudinary = new HashMap<Object, Object>();
        optionsCloudinary.put(
                "public_id",
                title);
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", Config4CloudStorage.CLOUD_NAME,
                "api_key", Config4CloudStorage.API_KEY,
                "api_secret", Config4CloudStorage.API_SECRET));
        Map uploadResult = null;
        try {
            uploadResult = cloudinary.uploader().upload(file.getBytes(), optionsCloudinary);
        } catch(Exception e){
            e.printStackTrace();
            throw new Exception("Can`t upload file!");
        }
        return (String)uploadResult.get("url");
    }

    /**
     *
     * @param photo downloaded file from telegram
     * @param userID id od user that send photo
     * @return url to photo at cloudinary
     * @throws Exception
     */
   /* public static String uploadPhoto(File photo, Integer userID) throws Exception{
        Map<Object, Object> optionsCloudinary = new HashMap<Object, Object>();
        optionsCloudinary.put(
                "public_id",
                userID+"||"+photo.fileId());
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", Config4CloudStorage.CLOUD_NAME,
                "api_key", Config4CloudStorage.API_KEY,
                "api_secret", Config4CloudStorage.API_SECRET));
        Map uploadResult = null;
        try {
            uploadResult = cloudinary.uploader().upload(BotMarket.bot.getFullFilePath(photo), optionsCloudinary);
        } catch(Exception e){
            e.printStackTrace();
            throw new Exception("Can`t upload file!");
        }
        return (String)uploadResult.get("url");
    }*/
}
