package org.amis.vibemusicserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author : KwokChichung
 * @description : 应用启动完成后打印自定义 Banner
 * @createDate : 2026/3/5
 */
@Slf4j
@Component
public class StartupBanner implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String banner = "\n\n" +
                " /$$    /$$ /$$ /$$                       /$$      /$$                     /$$          \n" +
                " | $$   | $$|__/| $$                      | $$$    /$$$                    |__/          \n" +
                " | $$   | $$ /$$| $$$$$$$   /$$$$$$       | $$$$  /$$$$ /$$   /$$  /$$$$$$$ /$$  /$$$$$$$ \n" +
                " |  $$ / $$/| $$| $$__  $$ /$$__  $$      | $$ $$/$$ $$| $$  | $$ /$$_____/| $$ /$$_____/ \n" +
                "  \\  $$ $$/ | $$| $$  \\ $$| $$$$$$$$      | $$  $$$| $$| $$  | $$|  $$$$$$ | $$| $$      \n" +
                "   \\  $$$/  | $$| $$  | $$| $$_____/      | $$\\  $ | $$| $$  | $$ \\____  $$| $$| $$      \n" +
                "    \\  $/   | $$| $$$$$$$/|  $$$$$$$      | $$ \\/  | $$|  $$$$$$/ /$$$$$$$/| $$|  $$$$$$$ \n" +
                "     \\_/    |__/|_______/  \\_______/      |__/     |__/ \\______/ |_______/ |__/ \\_______/ \n" +
                "\n" +
                "  :: Spring Boot ::  (v" + SpringBootVersion.getVersion() + ")\n" +
                "  :: App Version   ::  (v" + event.getApplicationContext().getEnvironment().getProperty("app.version") + ")\n" +
                "  :: Application Started Successfully ::\n\n";

        log.info(banner);
    }
}
