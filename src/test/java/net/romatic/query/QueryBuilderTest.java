package net.romatic.query;

import junit.framework.TestCase;
import net.romatic.TestCaseBase;
import net.romatic.query.grammar.MySqlGrammar;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author zhrlnt@gmail.com
 */
public class QueryBuilderTest extends TestCaseBase {

    protected QueryBuilder newQuery() {
        QueryBuilder query = new QueryBuilder();
        query.setGrammar(new MySqlGrammar());
        return query;
    }

    @Test
    public void testBasicSQL() {
        QueryBuilder query = new QueryBuilder();
        query.setGrammar(new MySqlGrammar());
        query.select("id", "name")
                .from("user")
                .where("mobile", "like", "188%")
                .where("post_number", 10)
                .whereSub("id", "in", post -> post.select("author_id").from("post").where("name", "like", "%Java%"), "and")
                .whereIn("age", 18, 24)
                .orWhereIn("age", "25", "26")
                .whereNull("deleted_at")
                .orderBy("age")
                .orderBy("id", false)
                .offset(100)
                .limit(10);
        String sql = query.toSQL();
        System.out.println(sql);
        Arrays.stream(query.getFlatBindings()).forEach(System.out::println);

        //Assert.assertEquals(sql, "SELECT `id`, `name` FROM `user` WHERE `mobile` like ? AND `post_number` = ? AND `age` IN (?, ?) OR `age` IN (?, ?) AND `deleted_at` IS NULL ORDER BY `age` ASE, `id` DESC LIMIT 10 OFFSET 100");
    }

    @Test
    public void testSub() {
        QueryBuilder query1 = newQuery();

        QueryBuilder query2 = newQuery();
        query2.from("user").where("id", 1);

        query1.where("author_id", "in", query2);

        dump(query1.toSQL());
    }

    @Test
    public <T> void testRunSelect() {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.setGrammar(new MySqlGrammar());
        queryBuilder.select()
                .from("test")
                .where("id", "<", 10)
                .where("name", "like", "%i r%");

        T[] results = queryBuilder.get();
        for (T row : results) {
            System.out.println(row);
        }
    }

    @Test
    public void testForDemo() {
        System.out.println();
    }
}
