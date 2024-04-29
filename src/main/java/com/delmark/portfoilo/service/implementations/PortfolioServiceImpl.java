package com.delmark.portfoilo.service.implementations;

import com.delmark.portfoilo.exceptions.*;
import com.delmark.portfoilo.models.DTO.PortfolioDto;
import com.delmark.portfoilo.models.Portfolio;
import com.delmark.portfoilo.models.Techs;
import com.delmark.portfoilo.models.User;
import com.delmark.portfoilo.repository.PortfolioRepository;
import com.delmark.portfoilo.repository.RolesRepository;
import com.delmark.portfoilo.repository.TechRepository;
import com.delmark.portfoilo.repository.UserRepository;
import com.delmark.portfoilo.service.interfaces.PortfolioService;
import com.delmark.portfoilo.utils.CustomMapper;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;
    private TechRepository techRepository;
    private final RolesRepository rolesRepository;
    public final CustomMapper customMapper;

    @Override
    public Portfolio getPortfolioByUser(String username) {
        if (userRepository.existsByUsername(username)) {
            Optional<Portfolio> portfolio = portfolioRepository.findByUser(userRepository.findByUsername(username).get());
            if (portfolio.isPresent()) {
                return portfolio.get();
            }
            else {
                throw new UserDoesNotHavePortfolioException();
            }
        }
        else {
            throw new UsernameNotFoundException(username);
        }
    }

    @Override
    public Portfolio getPortfolio(Long id) {
        Optional<Portfolio> portfolio = portfolioRepository.findById(id);
        if (portfolio.isPresent()) {
            return portfolio.get();
        }
        else {
            throw new NoSuchPortfolioException();
        }
    }

    @Override
    public Portfolio portfolioCreation(PortfolioDto dto) {
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        if (portfolioRepository.findByUser(sessionUser).isPresent()) {
            throw new UserAlreadyHavePortfolioException();
        }

        Portfolio newPortfolio = new Portfolio()
                .setName(dto.getName())
                .setMiddleName(dto.getMiddleName())
                .setSurname(dto.getSurname())
                .setAboutUser(dto.getAboutUser())
                .setEmail(dto.getEmail())
                .setEducation(dto.getEducation())
                .setPhone(dto.getPhone())
                .setSiteUrl(dto.getSiteUrl())
                .setUser(sessionUser);

        return portfolioRepository.save(newPortfolio);
    }

    // TODO: Создать отдельные исключения и их обработчики
    @Override
    public Portfolio addTechToPortfolio(Long portfolioId, Long techId) {
        Techs tech = techRepository.findById(techId).orElseThrow(NoSuchTechException::new);
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(NoSuchPortfolioException::new);

        // Проверка на доступ к портфолио
        if (!portfolio.getUser().getId().equals(sessionUser.getId())) {
            throw new AccessDeniedException("У пользователя нет доступа к этму портфолио");
        }

        // Добавление технологии в портфолио, если её там ещё нет
        if (!portfolio.getTechses().contains(tech)) {
            portfolio.getTechses().add(tech);
            return portfolioRepository.save(portfolio);
        }

        throw new TechAlreadyInPortfolioException();
        }

    @Override
    public Portfolio portfolioEdit(Long id, PortfolioDto dto) {
        Portfolio portfolio = portfolioRepository.findById(id).orElseThrow(NoSuchPortfolioException::new);
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Проверка на доступ к портфолио
        if (!portfolio.getUser().getId().equals(sessionUser.getId())) {
            throw new AccessDeniedException("У пользователя нет доступа к этму портфолио");
        }

        // Редактирование портфолио с помощью маппера
        customMapper.updatePortfolioFromDTO(dto, portfolio);
        return portfolioRepository.save(portfolio);
    }

    @Override
    public void deletePortfolio(Long id) {
        Portfolio portfolio = portfolioRepository.findById(id).orElseThrow(NoSuchPortfolioException::new);
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Проверка на доступ к портфолио
        if (!portfolio.getUser().getId().equals(sessionUser.getId())) {
            throw new AccessDeniedException("У пользователя нет доступа к этму портфолио");
        }

        portfolioRepository.delete(portfolio);
    }
}
