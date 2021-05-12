package reminator.EdtBot.bot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import reminator.EdtBot.Categories.OtherCategory;
import reminator.EdtBot.Categories.Category;
import reminator.EdtBot.Categories.EdtCategory;
import reminator.EdtBot.Commands.*;
import reminator.EdtBot.Commands.enums.Commands;
import reminator.EdtBot.utils.HTTPRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

public class Controller extends ListenerAdapter {

    public Controller() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris"));
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.isFromGuild()) return;

        event.getAuthor().openPrivateChannel()
                .flatMap(channel -> channel.sendMessage("3"))
                .delay(Duration.ofSeconds(1))
                .flatMap(channel -> channel.editMessage("2"))
                .delay(Duration.ofSeconds(1))
                .flatMap(channel -> channel.editMessage("1"))
                .delay(Duration.ofSeconds(1))
                .flatMap(Message::delete)
                .queue();
    }

    @Override
    public void onGuildMessageUpdate(@NotNull GuildMessageUpdateEvent event) {
        System.out.println("test");
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;


        String p =
                null;
        try {
            p = new HTTPRequest("https://twitter.com/i/api/2/timeline/conversation/1392212382831816705.json?include_profile_interstitial_type=1&include_blocking=1&include_blocked_by=1&include_followed_by=1&include_want_retweets=1&include_mute_edge=1&include_can_dm=1&include_can_media_tag=1&skip_status=1&cards_platform=Web-12&include_cards=1&include_ext_alt_text=true&include_quote_count=true&include_reply_count=1&tweet_mode=extended&include_entities=true&include_user_entities=true&include_ext_media_color=true&include_ext_media_availability=true&send_error_codes=true&simple_quoted_tweet=true&count=20&include_ext_has_birdwatch_notes=false&ext=mediaStats%2ChighlightedLabel")
                    .withHeader("accept-language", "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7")
                    .withHeader("authorization", "Bearer AAAAAAAAAAAAAAAAAAAAANRILgAAAAAAnNwIzUejRCOuH5E6I8xnZz4puTs%3D1Zv7ttfk8LF81IUq16cHjhLTvJu4FA33AGWWjCpTnA")
                    .withHeader("cookie", "_sl=1; personalization_id=\"v1_oRZugRsMECI++UDH3RMehQ==\"; guest_id=v1%3A162085596435924474; ct0=a4014bb283e5dcffdd9e1a5070026109; _twitter_sess=BAh7CSIKZmxhc2hJQzonQWN0aW9uQ29udHJvbGxlcjo6Rmxhc2g6OkZsYXNo%250ASGFzaHsABjoKQHVzZWR7ADoPY3JlYXRlZF9hdGwrCExKi2J5AToMY3NyZl9p%250AZCIlM2QwMWI3OWFiNmVmMmFhYmEwYmMxODk1OWU5YTczMTc6B2lkIiVlM2Nl%250AMzZiMjRhM2Q0ZGJmY2Q1M2IxZTdkNjVhZGViOA%253D%253D--678ba1feeb5cebc249025a654fe16fdddae3a3e2; gt=1392596973203693578")
                    .withHeader("referer", "https://twitter.com/JulieOudet/status/1392212382831816705")
                    .withHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"90\", \"Google Chrome\";v=\"90\"")
                    .withHeader("sec-ch-ua-mobile", "?0")
                    .withHeader("sec-fetch-dest", "empty")
                    .withHeader("sec-fetch-mode", "cors")
                    .withHeader("sec-fetch-site", "same-origin")
                    .withHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36")
                    .withHeader("x-csrf-token", "a4014bb283e5dcffdd9e1a5070026109")
                    .withHeader("x-guest-token", "1392596973203693578")
                    .withHeader("x-twitter-active-user", "yes")
                    .withHeader("x-twitter-client-language", "fr")
                    .GET();

            JSONObject page = new JSONObject(p);
            String conversation = page.getJSONObject("timeline").getString("id");
            String id = conversation.split("-")[1];
            JSONObject tweet = page.getJSONObject("globalObjects").getJSONObject("tweets").getJSONObject(id);
            System.out.println(tweet.getString("full_text"));
            event.getChannel().sendMessage(tweet.getString("full_text")).queue();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> args = new ArrayList<>(Arrays.asList(event.getMessage().getContentRaw().split("\\s+")));

        String command = args.get(0);

        for (Commands c : Commands.values()) {
            Command cmd = c.getCommand();
            String prefix = cmd.getPrefix();
            String label = cmd.getLabel();
            String prefixLabel = prefix + label;

            String[] separation = command.split(prefix);

            if (prefixLabel.equalsIgnoreCase(command) || separation.length > 1 && cmd.isAlias(separation[1])) {
                args.remove(0);
                c.getCommand().execute(event, event.getAuthor(), event.getChannel(), args);
                return;
            }
        }
    }
}