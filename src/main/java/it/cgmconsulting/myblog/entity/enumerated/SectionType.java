package it.cgmconsulting.myblog.entity.enumerated;

public enum SectionType {
    /*
    * Usiamo una coppia di caratteri perchè il db dichiara
    * l'enum type come stringa, se usassimo un solo carattere invece
    * questo sarebbe trattato come un char da java.
    * L'utilizzo di due caratteri evita che si verifichi questa situazione
    * ed inoltre occupa meno spazio sul db.
    * */
    HE, //HEADER
    GE, //GENERIC
    FO //FOOTER
}
