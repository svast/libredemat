grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
                      xml: ['text/xml', 'application/xml'],
                      text: 'text/plain',
                      js: 'text/javascript',
                      rss: 'application/rss+xml',
                      atom: 'application/atom+xml',
                      css: 'text/css',
                      csv: 'text/csv',
                      all: '*/*',
                      json: ['application/json','text/json'],
                      form: 'application/x-www-form-urlencoded',
                      multipartForm: 'multipart/form-data'
                    ]
grails.mime.use.accept.header = true

// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true

grails.views.default.codec="none" // none, html, base64
grails.views.gsp.encoding="UTF-8"
grails.converters.encoding="UTF-8"

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder=true

grails.gsp.enable.reload = false

// Debug Plugin
/*
grails.debug.system = true
grails.debug.stats = true
grails.debug.params = true
grails.debug.headers = true
grails.debug.controller = true
grails.debug.session = true
grails.debug.requestAttributes = true
grails.debug.model = true
*/

//jcaptcha Plugin
import java.awt.Font
import java.awt.Color

import com.octo.captcha.service.multitype.GenericManageableCaptchaService
import com.octo.captcha.engine.GenericCaptchaEngine
import com.octo.captcha.image.gimpy.GimpyFactory
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator
import com.octo.captcha.component.image.backgroundgenerator.GradientBackgroundGenerator
import com.octo.captcha.component.image.color.SingleColorGenerator
import com.octo.captcha.component.image.textpaster.NonLinearTextPaster

jcaptchas {
	captchaImage = new GenericManageableCaptchaService(
		new GenericCaptchaEngine(
			new GimpyFactory(
				new RandomWordGenerator(
					"abcdefghijkmnpqrstuvwxyz23456789"
				),
				new ComposedWordToImage(
					new RandomFontGenerator(
						20, // min font size
						30, // max font size
						[new Font("Arial", 0, 10)] as Font[]
					),
					new GradientBackgroundGenerator(
						150, // width
						50, // height
						new SingleColorGenerator(Color.white),
						new SingleColorGenerator(Color.lightGray)
					),
					new NonLinearTextPaster(
						6, // minimal length of text
						6, // maximal length of text
						Color.black
					)
				)
			)
		),
		180, // minGuarantedStorageDelayInSeconds
		180000 // maxCaptchaStoreSize
	)
}

environments {
  development {
		grails.config.locations = [ "file:${basedir}/${appName}-config.properties"]
	}
	production {
		grails.config.locations = [ "classpath:${appName}-config.properties"]
	}
}
