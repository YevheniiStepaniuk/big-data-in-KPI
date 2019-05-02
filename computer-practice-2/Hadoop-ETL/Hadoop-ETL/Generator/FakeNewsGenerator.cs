using System.Collections.Generic;
using Bogus;
using Bogus.Extensions;

namespace Hadoop_ETL.Generator
{
    public class FakeNewsGenerator
    {
        private readonly Faker<Entity> _generator;

        public FakeNewsGenerator()
        {
            _generator = new Faker<Entity>();

            var authorFaker = new Faker<Author>();

            authorFaker
                .RuleFor(f => f.FirstName, f => f.Name.FirstName())
                .RuleFor(f => f.LastName, f => f.Name.LastName())
                .RuleFor(f => f.Title, f => f.Name.JobTitle());

            var imageFaker = new Faker<Logo>();

            imageFaker
                .RuleFor(f => f.Link, f => f.Image.LoremPixelUrl())
                .RuleFor(f => f.Alt, f => f.Lorem.Word());

            _generator.RuleFor(f => f.AddedDate, f => f.Date.Past(1))
                .RuleFor(f => f.Category, f => f.Random.Enum<NewsCategory>())
                .RuleFor(f => f.Type, f => f.Random.Enum<NewsType>())
                .RuleFor(f => f.Country, f => f.Address.Country())
                .RuleFor(f => f.Description, f => f.Lorem.Sentences(2))
                .RuleFor(f => f.Title, f => f.Lorem.Sentence(4))
                .RuleFor(f => f.Priority, f => f.Random.Int(0, 100))
                .RuleFor(f => f.Language, f => f.Hacker.Random.RandomLocale())
                .RuleFor(f => f.LogoInfo, imageFaker)
                .RuleFor(f => f.AuthorInfo, authorFaker);
        }

        public IEnumerable<Entity> Generate(int min = 1, int max = 1000)
        {
            return _generator.GenerateBetween(min, max);
        }
    }
}