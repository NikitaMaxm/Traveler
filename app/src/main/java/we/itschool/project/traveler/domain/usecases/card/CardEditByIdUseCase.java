package we.itschool.project.traveler.domain.usecases.card;

import we.itschool.project.traveler.domain.entity.CardEntity;
import we.itschool.project.traveler.domain.repository.CardDomainRepository;

public class CardEditByIdUseCase {

    private final CardDomainRepository repository;

    public CardEditByIdUseCase(CardDomainRepository repository) {
        this.repository = repository;
    }

    public void cardEditById (CardEntity card){
        repository.cardEditById(card);
    }

}