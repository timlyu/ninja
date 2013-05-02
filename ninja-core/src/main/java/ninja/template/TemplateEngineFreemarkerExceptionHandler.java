/**
 * Copyright (C) 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ninja.template;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import ninja.utils.NinjaProperties;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * A general exception handler for Freemarker.
 * - Outputs a readable error in test / dev mode.
 * - Outputs a general error message in production.
 * 
 * @author ra
 */
@Singleton
public class TemplateEngineFreemarkerExceptionHandler implements
        TemplateExceptionHandler {

    private NinjaProperties ninjaProperties;
    private Logger logger;

    @Inject
    public TemplateEngineFreemarkerExceptionHandler(Logger logger,
                                                    NinjaProperties ninjaProperties) {
        this.logger = logger;
        this.ninjaProperties = ninjaProperties;
    }

    public void handleTemplateException(TemplateException te,
                                        Environment env,
                                        Writer out) {

        if (ninjaProperties.isProd()) {
            
            PrintWriter pw = (out instanceof PrintWriter) ? (PrintWriter) out
                    : new PrintWriter(out);
            pw.println(
                     "<script language=javascript>//\"></script>"
                    + "<script language=javascript>//\'></script>"
                    + "<script language=javascript>//\"></script>"
                    + "<script language=javascript>//\'></script>"
                    + "</title></xmp></script></noscript></style></object>"
                    + "</head></pre></table>"
                    + "</form></table></table></table></a></u></i></b>"
                    + "<div align=left "
                    + "style='background-color:#FFFF00; color:#FF0000; "
                    + "display:block; border-top:double; padding:2pt; "
                    + "font-size:medium; font-family:Arial,sans-serif; "
                    + "font-style: normal; font-variant: normal; "
                    + "font-weight: normal; text-decoration: none; "
                    + "text-transform: none'>");
            pw.println("<b style='font-size:medium'>Ooops. A really strange error occurred. Please contact admin if error persists.</b>");
            pw.println("</div></html>");
            pw.flush();
            pw.close();
            
            logger.log(Level.SEVERE, "Templating error. This should not happen in production", te);
            
            

        } else {
            
            // print out full stacktrace if we are in test or dev mode

            PrintWriter pw = (out instanceof PrintWriter) ? (PrintWriter) out
                    : new PrintWriter(out);
            pw.println("<!-- FREEMARKER ERROR MESSAGE STARTS HERE -->"
                    + "<script language=javascript>//\"></script>"
                    + "<script language=javascript>//\'></script>"
                    + "<script language=javascript>//\"></script>"
                    + "<script language=javascript>//\'></script>"
                    + "</title></xmp></script></noscript></style></object>"
                    + "</head></pre></table>"
                    + "</form></table></table></table></a></u></i></b>"
                    + "<div align=left "
                    + "style='background-color:#FFFF00; color:#FF0000; "
                    + "display:block; border-top:double; padding:2pt; "
                    + "font-size:medium; font-family:Arial,sans-serif; "
                    + "font-style: normal; font-variant: normal; "
                    + "font-weight: normal; text-decoration: none; "
                    + "text-transform: none'>"
                    + "<b style='font-size:medium'>FreeMarker template error!</b>"
                    + "<pre><xmp>");
            te.printStackTrace(pw);
            pw.println("</xmp></pre></div></html>");
            pw.flush();
            pw.close();
            
            logger.log(Level.SEVERE, "Templating error.", te);
        }

    }
}