package net.romatic.jade.net.romatic.query;

import junit.framework.TestCase;
import net.romatic.query.QueryBuilder;
import net.romatic.query.grammar.MySQLGrammar;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhrlnt@gmail.com
 */
public class QueryBuilderTest extends TestCase {

    @Test
    public void testBasicSQL() {
        QueryBuilder query = new QueryBuilder();
        query.setGrammar(new MySQLGrammar());
        query.select("id", "name")
                .from("user")
                .where("mobile", "like", "188%")
                .where("post_number", 10)
                .whereIn("age", 18, 24)
                .orWhereIn("age", "25", "26")
                .whereNull("deleted_at")
                .orderBy("age")
                .orderBy("id", false)
                .offset(100)
                .limit(10);
        String sql = query.toSQL();
        System.out.println(sql);

        //Assert.assertEquals(sql, "SELECT `id`, `name` FROM `user` WHERE `mobile` like ? AND `post_number` = ? AND `age` IN (?, ?) OR `age` IN (?, ?) AND `deleted_at` IS NULL ORDER BY `age` ASE, `id` DESC LIMIT 10 OFFSET 100");
    }

    @Test
    public <T> void testRunSelect() {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.setGrammar(new MySQLGrammar());
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
