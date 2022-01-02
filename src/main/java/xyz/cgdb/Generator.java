package xyz.cgdb;

import org.apache.commons.csv.CSVFormat;
import xyz.cgdb.records.Blog;
import xyz.cgdb.records.Contract;
import xyz.cgdb.records.Game;
import xyz.cgdb.records.Podcast;

import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Generator {
    final Map<String, List<Contract>> contracts = new HashMap<>();
    final List<Game> games = new ArrayList<>();
    final List<Podcast> podcasts = new ArrayList<>();
    final List<Blog> blogs = new ArrayList<>();

    final Freemarker freemarker;

    public Generator(String dir) {
        if (!dir.endsWith("/")) {
            dir += "/";
        }
        touchDir(dir + "games");
        this.freemarker = new Freemarker(dir);
    }

    void populate() throws Exception {
        readContracts();
        readGames();
        readPodcasts();
        readBlogs();
    }

    void readContracts() throws Exception {
        try (var reader = new FileReader("contracts.csv")) {
            var records = CSVFormat.Builder.create().setHeader().build().parse(reader);
            for (var record : records) {
                var slug = record.get("Slug");
                var contract = record.get("Contract");
                var chain = record.get("Chain");
                var name = record.get("Name");
                var market = record.get("Market");
                contracts.putIfAbsent(slug, new ArrayList<>());
                contracts.get(slug).add(new Contract(slug, contract, chain, name, market));
            }
        }
    }

    void readGames() throws Exception {
        try (var reader = new FileReader("games.csv")) {
            var records = CSVFormat.Builder.create().setHeader().build().parse(reader);
            for (var record : records) {
                var name = record.get("Name");
                var slug = record.get("Slug");
                var url = record.get("URL");
                var status = record.get("Status");
                games.add(new Game(name, slug, url, status, contracts.get(slug)));
            }
        }
        games.sort(Comparator.comparing(Game::name));
    }

    void readPodcasts() throws Exception {
        try (var reader = new FileReader("podcasts.csv")) {
            var records = CSVFormat.Builder.create().setHeader().build().parse(reader);
            for (var record : records) {
                var name = record.get("Name");
                var url = record.get("URL");
                var description = record.get("Description");
                podcasts.add(new Podcast(name, url, description));
            }
        }
        podcasts.sort(Comparator.comparing(Podcast::name));
    }

    void readBlogs() throws Exception {
        try (var reader = new FileReader("blogs.csv")) {
            var records = CSVFormat.Builder.create().setHeader().build().parse(reader);
            for (var record : records) {
                var name = record.get("Name");
                var url = record.get("URL");
                blogs.add(new Blog(name, url));
            }
        }
        blogs.sort(Comparator.comparing(Blog::name));
    }

    void generateIndex() throws Exception {
        var chains = new HashMap<String, Set<String>>();
        for (var game : games) {
            if (game.contracts() != null) {
                for (var contract : game.contracts()) {
                    chains.putIfAbsent(game.slug(), new HashSet<>());
                    chains.get(game.slug()).add(contract.chain());
                }
            }
        }
        freemarker.render("index.ftlh", "index.html", Map.of("games", games, "chains", chains));
    }

    void generateGames() throws Exception {
        for (var game : games) {
            var file = "games/" + game.slug() + ".html";
            freemarker.render("game.ftlh", file, Map.of("game", game));
        }
    }

    void touchDir(String dir) {
        var siteDir = new File(dir);
        if (!siteDir.exists()) siteDir.mkdirs();
    }

    void generatePodcasts() throws Exception {
        freemarker.render("podcasts.ftlh", "podcasts.html", Map.of("podcasts", podcasts));
    }

    void generateBlogs() throws Exception {
        freemarker.render("blogs.ftlh", "blogs.html", Map.of("blogs", blogs));
    }

    public static void main(String[] args) throws Exception {
        var generator = new Generator(args[0]);
        generator.populate();
        generator.generateIndex();
        generator.generateGames();
        generator.generatePodcasts();
        generator.generateBlogs();
    }
}
