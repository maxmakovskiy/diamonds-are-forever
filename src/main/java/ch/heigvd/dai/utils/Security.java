package ch.heigvd.dai.utils;

import java.security.SecureRandom;
import java.util.Arrays;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

public class Security {
  private static final int SALT_LEN = 16;
  private static final int NUM_ITERATIONS = 2;
  private static final int MEM_LIMIT = 66536;
  private static final int HASH_LEN = 32;
  private static final int PARALLELISM = 1;

  public static byte[] generateSalt() {
    SecureRandom secureRandom = new SecureRandom();
    byte[] salt = new byte[SALT_LEN];
    secureRandom.nextBytes(salt);
    return salt;
  }

  public static byte[] hash(final byte[] password, final byte[] salt) {
    Argon2Parameters.Builder builder =
        new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
            .withVersion(Argon2Parameters.ARGON2_VERSION_13)
            .withIterations(NUM_ITERATIONS)
            .withMemoryAsKB(MEM_LIMIT)
            .withParallelism(PARALLELISM)
            .withSalt(salt);

    Argon2BytesGenerator generate = new Argon2BytesGenerator();
    generate.init(builder.build());

    byte[] hashed = new byte[HASH_LEN];
    generate.generateBytes(password, hashed, 0, hashed.length);

    // store salt pre-appended to the password
    byte[] result = Arrays.copyOf(salt, salt.length + hashed.length);
    System.arraycopy(hashed, 0, result, salt.length, hashed.length);

    return result;
  }

  public static byte[] extractSalt(final byte[] hashedPassword) {
    return Arrays.copyOfRange(hashedPassword, 0, SALT_LEN);
  }
}
