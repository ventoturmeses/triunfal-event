using System.Collections.Generic;
using System.Linq;
using Microsoft.AspNetCore.Mvc;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using API.Models;
using API.Data;

namespace API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ConvidadoController : Controller
    {
        private readonly Context _context;
        public ConvidadoController(Context context)
        {
            // construtor
            _context = context;
        }

        [HttpGet]
        public ActionResult<List<Convidado>> GetAll()
        {
            return _context.Convidado.ToList();
        }

        [HttpGet("{nomeUsuario}/{idEvento}/")]
        public ActionResult<Convidado> GetByIdUsernameEvent(string nomeUsuario, string idEvento)
        {
            try
            {
                var convidado = _context.Convidado.Find(nomeUsuario, idEvento.ToUpper());

                if (convidado == null)
                    return NotFound();

                return convidado;
            }
            catch
            {
                return this.StatusCode(StatusCodes.Status500InternalServerError, "Falha no acesso ao banco de dados. ");
            }
        }

        
        [HttpGet("{nomeUsuario}")]
        public ActionResult<List<Convidado>> GetByIdUsername(string nomeUsuario)
        {
            try
            {
                //var convite = _context.Convite.ToList().Find();
                var convites = _context.Convidado.ToList().FindAll(i => i.nomeUsuario == nomeUsuario);

                if (convites == null)
                    return NotFound();

                return convites;
            }
            catch
            {
                return this.StatusCode(StatusCodes.Status500InternalServerError, "Falha no acesso ao banco de dados. ");
            }
        }

        [HttpGet("list/{idEvento}")]
        public ActionResult<List<Convidado>> GetByIdEvento(string idEvento)
        {
            try
            {
                //var convite = _context.Convite.ToList().Find();
                var convites = _context.Convidado.ToList().FindAll(i => i.idEvento == idEvento);
          

                if (convites == null)
                    return NotFound();

                return convites;
            }
            catch
            {
                return this.StatusCode(StatusCodes.Status500InternalServerError, "Falha no acesso ao banco de dados. ");
            }
        }

        [HttpPost]
        public async Task<ActionResult> PostConvidado(Convidado convidado)
        {
            try
            {
                _context.Convidado.Add(convidado);

                if (await _context.SaveChangesAsync() == 1)
                    return Created($"/api/convidado/{convidado.nomeUsuario}/{convidado.idEvento}", convidado);
            }
            catch
            {
                return this.StatusCode(StatusCodes.Status500InternalServerError, "Falha no acesso ao banco de dados.");
            }
            return BadRequest();
        }

        [HttpDelete("{nomeUsuario}/{idEvento}")]
        public async Task<ActionResult> Delete(string nomeUsuario, string idEvento)
        {
            try
            { 

                var convidado = _context.Convidado.Find(nomeUsuario, idEvento.ToUpper());

                if (convidado == null)
                    return NotFound();

                _context.Remove(convidado);
                await _context.SaveChangesAsync();
                return NoContent();
            }
            catch
            {
                return this.StatusCode(StatusCodes.Status500InternalServerError, "Falha no acesso ao banco de dados.");
            }
        }
    }
}
