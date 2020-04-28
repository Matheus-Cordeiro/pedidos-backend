package com.matheuscordeiro.pedidos.services;

import java.util.Date;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.matheuscordeiro.pedidos.domain.Cliente;
import com.matheuscordeiro.pedidos.domain.ItemPedido;
import com.matheuscordeiro.pedidos.domain.PagamentoBoleto;
import com.matheuscordeiro.pedidos.domain.Pedido;
import com.matheuscordeiro.pedidos.domain.Produto;
import com.matheuscordeiro.pedidos.domain.enums.EstadoPagamento;
import com.matheuscordeiro.pedidos.exceptions.AuthorizationException;
import com.matheuscordeiro.pedidos.exceptions.ObjectNotFoundException;
import com.matheuscordeiro.pedidos.repositories.ItemPedidoRepository;
import com.matheuscordeiro.pedidos.repositories.PagamentoRepository;
import com.matheuscordeiro.pedidos.repositories.PedidoRepository;
import com.matheuscordeiro.pedidos.security.UserSecurity;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteService clienteService;
	

	@Autowired
    private JavaMailSender javaMailSender;
	
	@Autowired
	TemplateEngine templateEngine;
	
	private static final Logger LOG = LoggerFactory.getLogger(SmtpEmailService.class);	
	
	private SmtpEmailService smtpEmailService = new SmtpEmailService();

	public Pedido findById(Integer id){
		Optional<Pedido> pedido = pedidoRepository.findById(id);
		return pedido.orElseThrow(() -> new ObjectNotFoundException(
		"Objeto não encontrado! Id: " + id + " | Tipo: " + Pedido.class.getName()));
	}
	
	public Pedido save(Pedido pedido){
		pedido.setId(null);
		pedido.setInstante(new Date());
		pedido.setCliente(clienteService.findById(pedido.getCliente().getId()));
		pedido.getPagamento().setEstadoPagamento(EstadoPagamento.PENDENTE);
		pedido.getPagamento().setPedido(pedido);
		if(pedido.getPagamento() instanceof PagamentoBoleto) {
			PagamentoBoleto tipoPagamento = (PagamentoBoleto) pedido.getPagamento();
			boletoService.preencherPagamentoBoleto(tipoPagamento, pedido.getInstante());
		}
		
		pedido = pedidoRepository.save(pedido);
		pagamentoRepository.save(pedido.getPagamento());
		
		for(ItemPedido itemPedido : pedido.getItens()) {
			itemPedido.setDesconto(0.0);
			Produto produto = produtoService.findById(itemPedido.getProduto().getId());
			itemPedido.setProduto(produto);
			itemPedido.setPreco(itemPedido.getProduto().getPreco());
			itemPedido.setPedido(pedido);
		}
		itemPedidoRepository.saveAll(pedido.getItens());
		
		LOG.info("Enviando email para " + pedido.getCliente().getEmail());
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);  
			mimeMessageHelper.setTo(pedido.getCliente().getEmail());
			mimeMessageHelper.setSubject("Pedido Confirmado - Código: " + pedido.getId());
			mimeMessageHelper.setSentDate(new Date(System.currentTimeMillis()));
			Context context = new Context();
			context.setVariable("pedido", pedido);
			mimeMessageHelper.setText(templateEngine.process("email/confirmacaoPedido", context), true);
			javaMailSender.send(mimeMessage);
			LOG.info("Email enviado");
		
		}catch(MessagingException e) {
			LOG.info("Falha ao enviar email html.");
			LOG.info("Enviando email padrão para " + pedido.getCliente().getEmail());
			SimpleMailMessage sm = smtpEmailService.prepareEmail(pedido);	
			javaMailSender.send(sm);
			LOG.info("Email enviado");
		}
		return pedido;
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage,String direction, String orderBy){
		UserSecurity userSecurity = UserService.getUsuarioLogado();
		if(userSecurity == null) {
			throw new AuthorizationException("Acesso negado!");
		}
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente = clienteService.findById(userSecurity.getId());
		return pedidoRepository.findByCliente(cliente, pageRequest);
	}
}
