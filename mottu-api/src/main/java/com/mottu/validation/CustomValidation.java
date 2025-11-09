package com.mottu.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.util.regex.Pattern;

/**
 * Validações customizadas para o sistema Mottu
 */
public class CustomValidation {

    /**
     * Validação para placa brasileira (formato ABC1234 ou ABC1D23)
     */
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = PlacaBrasileiraValidator.class)
    @Documented
    public @interface PlacaBrasileira {
        String message() default "Placa deve seguir o padrão brasileiro (ABC1234 ou ABC1D23)";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    public static class PlacaBrasileiraValidator implements ConstraintValidator<PlacaBrasileira, String> {
        private static final Pattern PLACA_PATTERN = Pattern.compile("^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$");

        @Override
        public boolean isValid(String placa, ConstraintValidatorContext context) {
            if (placa == null || placa.trim().isEmpty()) {
                return false;
            }
            return PLACA_PATTERN.matcher(placa.toUpperCase()).matches();
        }
    }

    /**
     * Validação para chassi de moto
     */
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = ChassiValidator.class)
    @Documented
    public @interface ChassiValido {
        String message() default "Chassi deve ter entre 17 e 50 caracteres alfanuméricos";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    public static class ChassiValidator implements ConstraintValidator<ChassiValido, String> {
        private static final Pattern CHASSI_PATTERN = Pattern.compile("^[A-Z0-9]{17,50}$");

        @Override
        public boolean isValid(String chassi, ConstraintValidatorContext context) {
            if (chassi == null || chassi.trim().isEmpty()) {
                return false;
            }
            return CHASSI_PATTERN.matcher(chassi.toUpperCase()).matches();
        }
    }

    /**
     * Validação para ano de fabricação
     */
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = AnoFabricacaoValidator.class)
    @Documented
    public @interface AnoFabricacaoValido {
        String message() default "Ano de fabricação deve estar entre 1990 e o ano atual + 1";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    public static class AnoFabricacaoValidator implements ConstraintValidator<AnoFabricacaoValido, Integer> {
        @Override
        public boolean isValid(Integer ano, ConstraintValidatorContext context) {
            if (ano == null) {
                return false;
            }
            int anoAtual = java.time.Year.now().getValue();
            return ano >= 1990 && ano <= (anoAtual + 1);
        }
    }

    /**
     * Validação para percentual de bateria
     */
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = PercentualBateriaValidator.class)
    @Documented
    public @interface PercentualBateriaValido {
        String message() default "Percentual de bateria deve estar entre 0 e 100";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    public static class PercentualBateriaValidator implements ConstraintValidator<PercentualBateriaValido, java.math.BigDecimal> {
        @Override
        public boolean isValid(java.math.BigDecimal percentual, ConstraintValidatorContext context) {
            if (percentual == null) {
                return false;
            }
            return percentual.compareTo(java.math.BigDecimal.ZERO) >= 0 && 
                   percentual.compareTo(new java.math.BigDecimal("100")) <= 0;
        }
    }

    /**
     * Validação para email único no sistema
     */
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = EmailUnicoValidator.class)
    @Documented
    public @interface EmailUnico {
        String message() default "Este email já está em uso no sistema";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    public static class EmailUnicoValidator implements ConstraintValidator<EmailUnico, String> {
        // Nota: Esta validação precisaria de acesso ao repository
        // Por simplicidade, deixamos a validação para o service
        @Override
        public boolean isValid(String email, ConstraintValidatorContext context) {
            // Implementar validação no service layer
            return true;
        }
    }

    /**
     * Validação para nome de pátio único
     */
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = NomePatioUnicoValidator.class)
    @Documented
    public @interface NomePatioUnico {
        String message() default "Já existe um pátio com este nome";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    public static class NomePatioUnicoValidator implements ConstraintValidator<NomePatioUnico, String> {
        @Override
        public boolean isValid(String nome, ConstraintValidatorContext context) {
            // Implementar validação no service layer
            return true;
        }
    }

    /**
     * Validação para senha forte
     */
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = SenhaFortaValidator.class)
    @Documented
    public @interface SenhaForte {
        String message() default "Senha deve ter pelo menos 8 caracteres, incluindo maiúscula, minúscula e número";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    public static class SenhaFortaValidator implements ConstraintValidator<SenhaForte, String> {
        @Override
        public boolean isValid(String senha, ConstraintValidatorContext context) {
            if (senha == null || senha.length() < 8) {
                return false;
            }
            
            boolean temMaiuscula = senha.chars().anyMatch(Character::isUpperCase);
            boolean temMinuscula = senha.chars().anyMatch(Character::isLowerCase);
            boolean temNumero = senha.chars().anyMatch(Character::isDigit);
            
            return temMaiuscula && temMinuscula && temNumero;
        }
    }

    /**
     * Validação para coordenadas de posição
     */
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = CoordenadaValidator.class)
    @Documented
    public @interface CoordenadaValida {
        String message() default "Coordenada deve ser um valor numérico válido";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    public static class CoordenadaValidator implements ConstraintValidator<CoordenadaValida, java.math.BigDecimal> {
        @Override
        public boolean isValid(java.math.BigDecimal coordenada, ConstraintValidatorContext context) {
            if (coordenada == null) {
                return false;
            }
            // Validar se está dentro de um range razoável (-999999 a 999999)
            return coordenada.compareTo(new java.math.BigDecimal("-999999")) >= 0 && 
                   coordenada.compareTo(new java.math.BigDecimal("999999")) <= 0;
        }
    }

    /**
     * Utilitários de validação
     */
    public static class ValidationUtils {
        
        public static boolean isValidPlaca(String placa) {
            if (placa == null || placa.trim().isEmpty()) {
                return false;
            }
            return Pattern.matches("^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$", placa.toUpperCase());
        }
        
        public static boolean isValidEmail(String email) {
            if (email == null || email.trim().isEmpty()) {
                return false;
            }
            return Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", email);
        }
        
        public static boolean isValidChassi(String chassi) {
            if (chassi == null || chassi.trim().isEmpty()) {
                return false;
            }
            return Pattern.matches("^[A-Z0-9]{17,50}$", chassi.toUpperCase());
        }
        
        public static boolean isValidAnoFabricacao(Integer ano) {
            if (ano == null) {
                return false;
            }
            int anoAtual = java.time.Year.now().getValue();
            return ano >= 1990 && ano <= (anoAtual + 1);
        }
        
        public static String formatarPlaca(String placa) {
            if (placa == null) {
                return null;
            }
            return placa.toUpperCase().replaceAll("[^A-Z0-9]", "");
        }
        
        public static String formatarChassi(String chassi) {
            if (chassi == null) {
                return null;
            }
            return chassi.toUpperCase().replaceAll("[^A-Z0-9]", "");
        }
        
        public static String normalizarEmail(String email) {
            if (email == null) {
                return null;
            }
            return email.toLowerCase().trim();
        }
    }
}