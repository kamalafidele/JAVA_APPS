import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;


public class WebDownloader {
    public static void main(String[] args){
        Collection<String> urlsContainer;
        Scanner keyboard=new Scanner(System.in);
        String webLink,folderName;
        int countFilesNum=0;

        WebDownloader webDownloader=new WebDownloader();
        System.out.print("Enter web link: ");
        webLink=keyboard.nextLine();
        System.out.print("Enter destination: ");
        folderName=keyboard.nextLine();

        urlsContainer=webDownloader.extractLinks(webLink);

        System.out.println("Number of Found links: "+urlsContainer.size());


        for(String link : urlsContainer) {
            ++countFilesNum;
            System.out.println("Downloading content for link "+countFilesNum+" ......");
            webDownloader.download(link, "File_For_Link_" + countFilesNum+".html",folderName);
        }

        System.out.println("\nFinished extracting links and downloading content");
    }

    public Collection<String> extractLinks(String webLink){
        Collection<String> foundUrls=new ArrayList<>();

        try{
            URL url=new URL(webLink);

            URLConnection connection=url.openConnection();
            BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;

            while((line= reader.readLine())!=null){
                if(line.contains("<a href=\"http")){

                    String halfLink=line.substring(line.indexOf("href=\"")+6,line.lastIndexOf('"'));

                    if(halfLink.contains("\""))
                        foundUrls.add(halfLink.replace(halfLink.substring(halfLink.indexOf('"')),""));
                    else
                        foundUrls.add(halfLink);
                }
            }

        }catch(IOException exception){
            exception.printStackTrace();
        }
        return foundUrls;
    }

    public void download(String link,String fileName,String folderName){
        try{
            URL url=new URL(link);
            File file=new File(folderName);
            if(!file.exists())
                file.mkdir();

            FileOutputStream outputStream=new FileOutputStream(folderName+"\\"+fileName);
            URLConnection connection=url.openConnection();
            BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String content;

            while((content=reader.readLine())!=null){
                outputStream.write(content.getBytes(StandardCharsets.UTF_8));
            }
        }catch(IOException exception){
            exception.printStackTrace();
        }
    }
    }