import java.awt.image.BufferedImage
import javax.imageio.ImageIO

/* css directory */

// migrate CSS FO file */
def cssFoFileWriter = new FileWriter('css/cssFo.css')
new File('../valdoise/css/cssFo.css').eachLine { line ->
    // first is menu color
    // second is body and menu hover colors
    cssFoFileWriter.write(line.replaceAll('#ff812b','#75a0c3').replaceAll('#b6aca5','#eee'))
    cssFoFileWriter.write('\n')
}
cssFoFileWriter.flush()
"git add css/cssFo.css".execute()

// delete old CSS files */
"git rm -rf css/new".execute()

/* html directory */
"git mv html/information.html html/informationFo.html".execute()

/* img directory */
["img/banner.jpg":"img/logoFo.png","img/logoBO.jpg":"img/logoBo.png","xsl/xsllogo.jpg":"img/logoPdf.png"].each { k,v ->
    System.err.println("reading " + k)
    File input = new File(k);
    BufferedImage image = ImageIO.read(input);
    File output = new File(v); 
    ImageIO.write(image, "png", output);
    "git rm $k".execute()
    "git add $v".execute()
}

/* local_referential directory */

// delete old local_referential_cvq.xml
"git rm local_referential/local_referential_cvq.xml".execute()

// remove obsolete attributes from others
// TODO : enghien has none of them




