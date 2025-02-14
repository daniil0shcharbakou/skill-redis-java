import org.redisson.Redisson;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.Random;

public class DatingSiteQueue {
    public static void main(String[] args) throws InterruptedException {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        RedissonClient redisson = Redisson.create(config);

        RList<Integer> queue = redisson.getList("USER_QUEUE");
        
        if (queue.isEmpty()) {
            for (int i = 1; i <= 20; i++) {
                queue.add(i);
            }
        }

        Random random = new Random();

        while (true) {
            Integer currentUser = queue.remove(0);
            System.out.println("— На главной странице показываем пользователя " + currentUser);

            if (random.nextInt(10) == 0) {
                int index = random.nextInt(queue.size());
                Integer payingUser = queue.remove(index);
                queue.add(0, payingUser);
                System.out.println("> Пользователь " + payingUser + " оплатил платную услугу");
            }

            queue.add(currentUser);

            Thread.sleep(1000);
        }
    }
}
